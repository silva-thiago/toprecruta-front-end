<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";

import { Form, FormField } from "@primevue/forms";
import InputText from "primevue/inputtext";
import Card from "primevue/card";
import Button from "primevue/button";
import Message from "primevue/message";

import { login } from "@/services/authService";

const router = useRouter();

const loginError = ref("");
const isLoading = ref(false);

const resolver = ({ values }: { values: Record<string, string> }) => {
  const errors: Record<string, { message: string }[]> = {};

  const email = (values.email ?? "").trim();

  if (!email) {
    errors.email = [{ message: "Email é obrigatório." }];
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
    errors.email = [{ message: "Informe um email válido." }];
  }

  const password = (values.password ?? "").trim();

  if (!password) {
    errors.password = [{ message: "Senha é obrigatória." }];
  } else if (password.length < 6) {
    errors.password = [{ message: "Mínimo de 6 caracteres." }];
  }

  return { values, errors };
};

const onSubmit = ({
  valid,
  values,
}: {
  valid: boolean;
  values: Record<string, string>;
}) => {
  if (!valid) return;

  loginError.value = "";
  isLoading.value = true;

  setTimeout(() => {
    const result = login(values.email.trim(), values.password);

    if (result.success) {
      router.push("/");
    } else {
      loginError.value = result.message;
    }
  }, 300);
};
</script>

<template>
  <section
    class="w-full max-w-110 bg-white dark:bg-neutral-900 p-10 rounded-2xl shadow-lg"
  >
    <Card role="region" class="dark:bg-neutral-900">
      <template #title class="mb-10">
        <h1
          class="text-center text-[32px] font-bold text-brand-dark leading-tight mb-2"
        >
          Usuários
        </h1>
        <p class="text-center text-lg font-medium text-accent">
          Painel Administrativo
        </p>
      </template>
      <template #content>
        <Form
          @submit="onSubmit"
          v-slot="$form"
          :resolver="resolver"
          :pt="{ root: { class: 'flex flex-col gap-6 mt-2' } }"
        >
          <FormField
            v-slot="$field"
            name="email"
            :pt="{ root: { class: 'flex flex-col gap-2' } }"
          >
            <label class="font-medium" for="email">Email</label>
            <InputText
              :pt="{
                root: {
                  class:
                    'w-full h-12 px-4 border border-neutral-200 rounded-lg bg-white text-base text-brand-dark placeholder:text-neutral-300 focus:outline-none focus:ring-2 focus:ring-accent/20 focus:border-accent transition-all',
                },
              }"
              id="email"
              placeholder="Digite seu email"
              type="email"
            />
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
              >{{ $field.error?.message }}</Message
            >
          </FormField>
          <FormField
            v-slot="$field"
            name="password"
            :pt="{ root: { class: 'flex flex-col gap-2' } }"
          >
            <label class="font-medium" for="password">Senha</label>
            <InputText
              :pt="{
                root: {
                  class:
                    'w-full h-12 px-4 border border-neutral-200 rounded-lg bg-white text-base text-brand-dark placeholder:text-neutral-300 focus:outline-none focus:ring-2 focus:ring-accent/20 focus:border-accent transition-all',
                },
              }"
              id="password"
              placeholder="Digite seu senha"
              type="password"
            />
            <Message
              v-if="$field?.invalid"
              severity="error"
              size="small"
              variant="simple"
              >{{ $field.error?.message }}</Message
            >
          </FormField>

          <Message
            v-if="loginError"
            severity="error"
            size="small"
            variant="simple"
            class="-mt-2"
          >
            {{ loginError }}
          </Message>

          <Button
            :label="isLoading ? 'Carregando...' : 'Entrar'"
            :loading="isLoading"
            :disabled="isLoading"
            :pt="{
              root: {
                class:
                  'w-full h-12 mt-2 bg-accent text-white font-semibold rounded-lg text-base shadow-sm hover:bg-accent/90 focus:outline-none focus:ring-4 focus:ring-accent/30 transition-all active:scale-[0.98]',
              },
            }"
            severity="primary"
            type="submit"
          />
        </Form>
      </template>
    </Card>
  </section>
</template>

<style scoped>
:deep(.p-card) {
  box-shadow: none;
}
</style>
