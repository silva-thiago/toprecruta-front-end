<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import type { FormInstance } from "@primevue/forms";

import Form from "@primevue/forms/form";
import { FormField } from "@primevue/forms";
import Fieldset from "primevue/fieldset";
import InputText from "primevue/inputtext";
import Select from "primevue/select";
import RadioButton from "primevue/radiobutton";
import RadioButtonGroup from "primevue/radiobuttongroup";
import Button from "primevue/button";
import Message from "primevue/message";

import { createUser, getUserById, updateUser } from "@/services/userService";
import {
  fetchAddressByZipcode,
  sanitizeZipcode,
} from "@/services/viaCepService";
import type { User } from "@/types/user";

const route = useRoute();
const router = useRouter();

const isEditMode = computed(() => route.name === "user-edit");

const formKey = ref(0);
const existingCreatedAt = ref(0);

const roles = [
  { label: "Dev Back-end", value: "Dev Back-end" },
  { label: "Dev Front-end", value: "Dev Front-end" },
  { label: "Dev Full Stack", value: "Dev Full Stack" },
  { label: "UX/UI Designer", value: "UX/UI Designer" },
];

// Bug 2 fix: controlled string for the masked date input (DD/MM/AAAA)
const birthdateInput = ref("");

const initialValues = ref({
  name: "",
  birthdate: "",
  gender: "",
  role: "",
  zipcode: "",
  street: "",
  neighborhood: "",
  city: "",
  state: "",
});

const onBirthdateInput = (event: Event) => {
  const input = event.target as HTMLInputElement;
  let digits = input.value.replace(/\D/g, "").slice(0, 8);

  if (digits.length > 4) {
    digits = `${digits.slice(0, 2)}/${digits.slice(2, 4)}/${digits.slice(4)}`;
  } else if (digits.length > 2) {
    digits = `${digits.slice(0, 2)}/${digits.slice(2)}`;
  }

  birthdateInput.value = digits;
  formRef.value?.setFieldValue("birthdate", digits);
};

const resolver = ({ values }: { values: Record<string, any> }) => {
  const errors: Record<string, { message: string }[]> = {};

  const requiredFields = [
    "name",
    "birthdate",
    "gender",
    "role",
    "zipcode",
    "street",
    "neighborhood",
    "city",
    "state",
  ];

  requiredFields.forEach((field) => {
    const value = values[field];

    if (value === null || value === undefined || value === "") {
      errors[field] = [{ message: "Campo obrigatório." }];
    }
  });

  if (values.birthdate) {
    const localDatePattern = String(values.birthdate).match(
      /^(\d{2})\/(\d{2})\/(\d{4})$/,
    );
    if (!localDatePattern) {
      errors.birthdate = [
        { message: "Data inválida. Use o formato DD/MM/AAAA." },
      ];
    } else {
      const [, day, month, year] = localDatePattern.map(Number);
      const date = new Date(year, month - 1, day);
      if (
        date.getFullYear() !== year ||
        date.getMonth() + 1 !== month ||
        date.getDate() !== day
      ) {
        errors.birthdate = [{ message: "Data inválida." }];
      }
    }
  }

  if (values.name && values.name.length > 100) {
    errors.name = [{ message: "Nome deve ter no máximo 100 caracteres." }];
  }

  const zipcode = String(values.zipcode ?? "").replace(/\D/g, "");
  if (values.zipcode && !/^\d{8}$/.test(zipcode)) {
    errors.zipcode = [{ message: "Formato de CEP inválido." }];
  }

  return { values, errors };
};

const formRef = ref<FormInstance | null>(null);

const zipcodeError = ref("");

const fillAddress = async () => {
  const form = formRef.value;
  if (!form) return;

  try {
    zipcodeError.value = "";

    const zipcodeValue = String(form.states.zipcode?.value ?? "");
    const data = await fetchAddressByZipcode(zipcodeValue);

    form.setFieldValue("street", data.logradouro ?? "");
    form.setFieldValue("neighborhood", data.bairro ?? "");
    form.setFieldValue("city", data.localidade ?? "");
    form.setFieldValue("state", data.uf ?? "");

    await form.validate(["zipcode", "street", "neighborhood", "city", "state"]);
  } catch (error) {
    form.setFieldValue("street", "");
    form.setFieldValue("neighborhood", "");
    form.setFieldValue("city", "");
    form.setFieldValue("state", "");

    zipcodeError.value =
      error instanceof Error ? error.message : "Erro ao buscar CEP.";

    await form.validate(["zipcode", "street", "neighborhood", "city", "state"]);
  }
};

const formatBirthdateToISO = (value: unknown): string => {
  if (!value) return "";

  const trimmed = String(value).trim();

  if (/^\d{4}-\d{2}-\d{2}$/.test(trimmed)) return trimmed;

  const localDatePattern = trimmed.match(/^(\d{2})\/(\d{2})\/(\d{4})$/);
  if (localDatePattern) {
    const [, day, month, year] = localDatePattern;
    return `${year}-${month}-${day}`;
  }

  return "";
};

const calculateAge = (birthdate: string): number => {
  if (!birthdate) return 0;

  const [year, month, day] = birthdate.split("-").map(Number);

  if (!year || !month || !day) return 0;

  const today = new Date();
  let age = today.getFullYear() - year;

  const currentMonth = today.getMonth() + 1;
  const currentDay = today.getDate();

  if (currentMonth < month || (currentMonth === month && currentDay < day)) {
    age--;
  }

  return age;
};

const onSubmit = ({
  valid,
  values,
}: {
  valid: boolean;
  values: Record<string, any>;
}) => {
  if (!valid) return;

  const normalizedBirthdate = formatBirthdateToISO(values.birthdate);

  const user: User = {
    id: isEditMode.value ? String(route.params.id) : Date.now().toString(),
    name: String(values.name ?? "").trim(),
    birthdate: normalizedBirthdate,
    age: calculateAge(normalizedBirthdate),
    gender: String(values.gender ?? ""),
    role: String(values.role ?? ""),
    zipcode: sanitizeZipcode(String(values.zipcode ?? "")),
    street: String(values.street ?? "").trim(),
    neighborhood: String(values.neighborhood ?? "").trim(),
    city: String(values.city ?? "").trim(),
    state: String(values.state ?? "").trim(),
    createdAt: isEditMode.value ? existingCreatedAt.value : Date.now(),
  };

  if (isEditMode.value) {
    updateUser(user);
  } else {
    createUser(user);
  }

  router.push("/");
};

onMounted(() => {
  if (!isEditMode.value) return;

  const user = getUserById(String(route.params.id));

  if (!user) {
    router.push("/");
    return;
  }

  existingCreatedAt.value = user.createdAt;

  let birthdateForInput = "";

  if (user.birthdate) {
    const parts = user.birthdate.match(/^(\d{4})-(\d{2})-(\d{2})$/);

    if (parts) {
      birthdateForInput = `${parts[3]}/${parts[2]}/${parts[1]}`;
    }
  }
  birthdateInput.value = birthdateForInput;

  initialValues.value = {
    name: user.name,
    birthdate: birthdateForInput,
    gender: user.gender,
    role: user.role,
    zipcode: user.zipcode,
    street: user.street,
    neighborhood: user.neighborhood,
    city: user.city,
    state: user.state,
  };

  formKey.value++;
});
</script>

<template>
  <section class="container min-h-svh py-8">
    <div class="mb-8 border-b border-border-strong pb-4">
      <h1 class="text-2xl font-bold text-text-h">
        {{ isEditMode ? "Editar usuário" : "Adicionar usuário" }}
      </h1>
    </div>

    <Form
      ref="formRef"
      @submit="onSubmit"
      v-slot="$form"
      :key="formKey"
      :initialValues="initialValues"
      :resolver="resolver"
      :pt="{ root: { class: 'flex flex-col max-w-4xl mx-auto gap-6' } }"
    >
      <Fieldset
        :pt="{
          root: {
            class: 'bg-surface-soft p-10 rounded-2xl shadow-sm gap-12',
          },
          content: { class: 'flex flex-col gap-4' },
        }"
      >
        <div class="flex flex-col lg:flex-row gap-6">
          <FormField
            v-slot="$field"
            :pt="{
              root: { class: 'col-span-8 flex flex-auto flex-col gap-2' },
            }"
            name="name"
          >
            <label for="name">Nome</label>
            <InputText
              :pt="{ root: { class: 'w-full h-12 rounded' } }"
              fluid
              id="name"
              placeholder="Digite o nome do usário"
              type="text"
            />
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
            >
              {{ $field.error?.message }}
            </Message>
          </FormField>

          <FormField
            v-slot="$field"
            :pt="{
              root: { class: 'col-span-4 flex flex-col gap-2' },
            }"
            name="gender"
          >
            <label for="gender">Gênero</label>
            <RadioButtonGroup
              :pt="{ root: { class: 'flex items-center gap-6 h-12' } }"
              name="gender"
            >
              <div class="flex items-center gap-2">
                <label for="male">Masculino</label>
                <RadioButton
                  inputId="male"
                  size="large"
                  variant="outlined"
                  value="Masculino"
                />
              </div>
              <div class="flex items-center gap-2">
                <label for="female">Feminino</label>
                <RadioButton
                  inputId="female"
                  size="large"
                  variant="outlined"
                  value="Feminino"
                />
              </div>
            </RadioButtonGroup>
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
            >
              {{ $field.error?.message }}
            </Message>
          </FormField>
        </div>

        <div class="flex flex-col lg:flex-row gap-6">
          <FormField
            v-slot="$field"
            :pt="{ root: { class: 'col-span-3 flex flex-col gap-2' } }"
            name="birthdate"
          >
            <label for="birthdate">Data de Nascimento</label>
            <InputText
              @input="onBirthdateInput"
              :value="birthdateInput"
              :pt="{ root: { class: 'w-full h-12 rounded' } }"
              fluid
              id="birthdate"
              inputmode="numeric"
              maxlength="10"
              placeholder="DD/MM/AAAA"
            />
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
            >
              {{ $field.error?.message }}
            </Message>
          </FormField>

          <FormField
            v-slot="$field"
            :pt="{
              root: { class: 'col-span-9 flex flex-auto flex-col gap-2' },
            }"
            name="role"
          >
            <label for="role">Função</label>
            <Select
              :options="roles"
              :pt="{
                root: { class: 'w-full' },
                label: { class: 'flex items-center h-12 px-3' },
                dropdown: { class: 'w-12 flex items-center justify-center' },
              }"
              fluid
              name="role"
              optionLabel="label"
              optionValue="value"
              inputId="role"
              placeholder="Selecione..."
            />
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
            >
              {{ $field.error?.message }}
            </Message>
          </FormField>
        </div>

        <p class="text-sm font-semibold text-brand-dark col-span-12 mt-4">
          Endereço
        </p>

        <div class="flex flex-col lg:flex-row gap-6">
          <FormField
            v-slot="$field"
            :pt="{ root: { class: 'col-span-4 flex flex-col gap-2' } }"
            name="zipcode"
          >
            <label for="zipcode">CEP</label>
            <InputText
              @blur="fillAddress"
              :pt="{ root: { class: 'w-full h-12 rounded' } }"
              fluid
              data-zipcode
              id="zipcode"
              placeholder="Digite o CEP"
              type="text"
            />
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
            >
              {{ $field.error?.message }}
            </Message>
          </FormField>

          <FormField
            v-slot="$field"
            :pt="{
              root: { class: 'col-span-8 flex flex-auto flex-col gap-2' },
            }"
            name="street"
          >
            <label for="street">Rua</label>
            <InputText
              :pt="{
                root: {
                  class:
                    'w-full h-12 rounded bg-neutral-100/95! text-neutral-600! dark:bg-neutral-800! dark:text-neutral-400!',
                },
              }"
              fluid
              data-street
              id="street"
              readonly
              type="text"
            />
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
            >
              {{ $field.error?.message }}
            </Message>
          </FormField>
        </div>

        <div class="flex flex-col lg:flex-row gap-6">
          <FormField
            v-slot="$field"
            :pt="{
              root: {
                class: 'col-span-4 flex flex-col gap-2',
              },
            }"
            name="neighborhood"
          >
            <label for="neighborhood">Bairro</label>
            <InputText
              :pt="{
                root: {
                  class:
                    'w-full h-12 rounded bg-neutral-100/95! text-neutral-600! dark:bg-neutral-800! dark:text-neutral-400!',
                },
              }"
              fluid
              data-neighborhood
              id="neighborhood"
              readonly
              type="text"
            />
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
            >
              {{ $field.error?.message }}
            </Message>
          </FormField>

          <FormField
            v-slot="$field"
            :pt="{
              root: {
                class: 'col-span-6 flex flex-auto flex-col gap-2',
              },
            }"
            name="city"
          >
            <label for="city">Cidade</label>
            <InputText
              :pt="{
                root: {
                  class:
                    'w-full h-12 rounded bg-neutral-100/95! text-neutral-600! dark:bg-neutral-800! dark:text-neutral-400!',
                },
              }"
              fluid
              data-city
              id="city"
              readonly
              type="text"
            />
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
            >
              {{ $field.error?.message }}
            </Message>
          </FormField>

          <FormField
            v-slot="$field"
            :pt="{
              root: {
                class: 'col-span-2 flex flex-col gap-2',
              },
            }"
            name="state"
          >
            <label for="state"> Estado </label>
            <InputText
              :pt="{
                root: {
                  class:
                    'w-full h-12 rounded bg-neutral-100/95! text-neutral-600! dark:bg-neutral-800! dark:text-neutral-400!',
                },
              }"
              fluid
              data-state
              id="state"
              readonly
              type="text"
            />
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
            >
              {{ $field.error?.message }}
            </Message>
          </FormField>
        </div>
      </Fieldset>

      <div class="flex flex-col md:flex-row justify-between gap-y-4">
        <Button
          @click="router.push('/')"
          label="Cancelar"
          severity="primary"
          variant="outlined"
          class="bg-transparent! border border-accent! text-accent! hover:bg-brand-muted! hover:text-surface! px-10 h-11 rounded"
          type="button"
        />
        <div class="flex flex-col md:flex-row gap-4">
          <Button
            label="Limpar"
            severity="primary"
            class="bg-neutral-400! text-neutral-50 px-10 h-11 rounded"
            type="reset"
          />
          <Button
            label="Salvar"
            severity="primary"
            class="bg-accent! px-10 h-11 rounded"
            type="submit"
          />
        </div>
      </div>
    </Form>
  </section>
</template>

<style scoped>
.divider {
  background: color-mix(in oklab, var(--border) 55%, white);
  height: 1px;
  margin-bottom: 22px;
  width: 100%;
}

:deep(input.p-component) {
  padding: 0.5rem;
}

:deep(.p-radiobutton-checked .p-radiobutton-box) {
  background: var(--accent);
}
</style>
