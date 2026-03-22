<script setup lang="ts">
import { computed, onMounted, reactive } from "vue";
import { useRoute, useRouter } from "vue-router";

import { Form, FormField } from "@primevue/forms";
import InputText from "primevue/inputtext";
import RadioButton from "primevue/radiobutton";
import RadioButtonGroup from "primevue/radiobuttongroup";
import Button from "primevue/button";

import { createUser, getUserById, updateUser } from "@/services/userService";
import type { User } from "@/types/user";

const route = useRoute();
const router = useRouter();

const isEditMode = computed(() => route.name === "user-edit");

const form = reactive<User>({
  id: "",
  name: "",
  birthdate: "",
  age: 0,
  gender: "",
  role: "",
  zipcode: "",
  street: "",
  neighborhood: "",
  city: "",
  state: "",
  createdAt: 0,
});

const calculateAge = (date: string): number => {
  if (!date) return 0;

  const today = new Date();
  const birthdate = new Date(date);
  let age = today.getFullYear() - birthdate.getFullYear();
  const monthDifference = today.getMonth() - birthdate.getMonth();

  if (
    monthDifference < 0 ||
    (monthDifference === 0 && today.getDate() < birthdate.getDate())
  ) {
    age--;
  }

  return age;
};

const fillForm = (user: User) => {
  Object.assign(form, user);
};

const resetForm = () => {
  form.id = "";
  form.name = "";
  form.birthdate = "";
  form.age = 0;
  form.gender = "";
  form.role = "";
  form.zipcode = "";
  form.street = "";
  form.neighborhood = "";
  form.city = "";
  form.state = "";
  form.createdAt = 0;
};

const handleSubmit = () => {
  form.age = calculateAge(form.birthdate);

  if (isEditMode.value) {
    updateUser({ ...form });
  } else {
    createUser({ ...form, id: Date.now().toString(), createdAt: Date.now() });
  }

  router.push("/");
};

onMounted(() => {
  if (!isEditMode.value) return;

  const id = String(route.params.id);
  const user = getUserById(id);

  if (!user) {
    router.push("/");
    return;
  }

  fillForm(user);
});
</script>

<template>
  <section class="container min-h-svh py-8">
    <div class="mb-8 border-b border-border-strong pb-4">
      <h1 class="text-2xl font-bold text-text-h">
        {{ isEditMode ? "Editar usuário" : "Adicionar usuário" }}
      </h1>
    </div>

    <Fieldset legend="Adicionar usuário">
      <Form
        @submit.prevent="handleSubmit"
        class="max-w-4xl mx-auto flex flex-col gap-10"
      >
        <div
          class="flex flex-col bg-surface-soft p-10 rounded-2xl shadow-sm gap-8"
        >
          <div class="flex flex-row gap-6">
            <FormField class="col-span-8 flex flex-auto flex-col gap-2">
              <label class="text-sm" for="name">Nome</label>
              <InputText
                v-model="form.name"
                class="w-full h-12 rounded"
                id="name"
                placeholder="Digite o nome do usário"
                type="text"
              />
            </FormField>

            <FormField class="col-span-4 flex flex-col gap-2">
              <label class="text-sm"> Gênero </label>
              <RadioButtonGroup
                name="gender"
                class="flex items-center gap-6 h-12"
              >
                <div class="flex items-center gap-2">
                  <label class="text-sm" for="male">Masculino</label>
                  <RadioButton
                    v-model="form.gender"
                    inputId="male"
                    class="bg-white rounded-full"
                    value="Masculino"
                    type="checkbox"
                  />
                </div>
                <div class="flex items-center gap-2">
                  <label class="text-sm" for="female">Feminino</label>
                  <RadioButton
                    v-model="form.gender"
                    inputId="female"
                    class="bg-white rounded-full"
                    value="Feminino"
                    type="checkbox"
                  />
                </div>
              </RadioButtonGroup>
            </FormField>
          </div>

          <div class="flex flex-row gap-6">
            <FormField class="col-span-3 flex flex-col gap-2">
              <label class="text-sm" for="name">Data de Nascimento</label>
              <InputText
                v-model="form.birthdate"
                class="w-full h-12 rounded"
                id="name"
                placeholder="DD/MM/AAAA"
                type="date"
              />
            </FormField>

            <FormField class="col-span-9 flex flex-auto flex-col gap-2">
              <label class="text-sm" for="role">Função</label>
              <select
                v-model="form.role"
                id="role"
                class="w-full h-12 rounded bg-white"
              >
                <option value="" disabled>Selecione...</option>
                <option value="Dev Back-end">Dev Back-end</option>
                <option value="Dev Front-end">Dev Front-end</option>
                <option value="Dev Full Stack">Dev Full Stack</option>
                <option value="UX/UI Designer">UX/UI Designer</option>
              </select>
            </FormField>
          </div>

          <p class="text-sm font-bold text-brand-dark col-span-12">Endereço</p>

          <div class="flex flex-row gap-6">
            <FormField name="zipcode" class="col-span-4 flex flex-col gap-2">
              <label class="text-sm" for="zipcode"> CEP </label>
              <InputText
                v-model="form.zipcode"
                id="zipcode"
                placeholder="Digite o CEP"
                class="w-full h-12 rounded"
                type="text"
              />
            </FormField>

            <FormField
              name="street"
              class="col-span-8 flex flex-auto flex-col gap-2"
            >
              <label class="text-sm" for="street"> Rua </label>
              <InputText
                v-model="form.street"
                id="street"
                class="w-full h-12 rounded bg-neutral-100/95! text-neutral-600!"
                readonly
                type="text"
              />
            </FormField>
          </div>

          <div class="flex flex-row gap-6">
            <FormField
              name="neighborhood"
              class="col-span-4 flex flex-col gap-2"
            >
              <label class="text-sm" for="neighborhood"> Bairro </label>
              <InputText
                v-model="form.neighborhood"
                id="neighborhood"
                class="w-full h-12 rounded bg-neutral-100/95! text-neutral-600!"
                readonly
                type="text"
              />
            </FormField>

            <FormField
              name="city"
              class="col-span-6 flex flex-auto flex-col gap-2"
            >
              <label class="text-sm" for="city"> Cidade </label>
              <InputText
                v-model="form.city"
                id="city"
                class="w-full h-12 rounded bg-neutral-100/95! text-neutral-600!"
                readonly
                type="text"
              />
            </FormField>

            <FormField name="state" class="col-span-2 flex flex-col gap-2">
              <label class="text-sm" for="state"> Estado </label>
              <InputText
                v-model="form.state"
                id="state"
                class="w-full h-12 rounded bg-neutral-100/95! text-neutral-600!"
                readonly
                type="text"
              />
            </FormField>
          </div>
        </div>

        <div class="flex justify-between">
          <Button
            label="Cancelar"
            severity="primary"
            variant="outlined"
            class="bg-transparent! border border-accent! text-accent! px-10 h-11 rounded"
            @click="router.push('/')"
          />
          <div class="flex gap-8">
            <Button
              label="Limpar"
              severity="primary"
              class="bg-neutral-400! text-neutral-50 px-10 h-11 rounded"
              @click="resetForm()"
            />
            <Button
              label="Salvar"
              severity="primary"
              class="bg-accent! px-10 h-11 rounded"
            />
          </div>
        </div>
      </Form>
    </Fieldset>
  </section>
</template>

<style scoped>
.divider {
  background: color-mix(in oklab, var(--border) 55%, white);
  height: 1px;
  margin-bottom: 22px;
  width: 100%;
}
</style>
