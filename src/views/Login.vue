<script setup lang="ts">
import { useRouter } from "vue-router";

import { Form, FormField } from "@primevue/forms";
import InputText from "primevue/inputtext";
import Card from "primevue/card";
import Button from "primevue/button";

const router = useRouter();

const handleLogin = () => {
  localStorage.setItem("auth", "true");
  router.push("/");
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
        <Form @submit="handleLogin" class="flex flex-col gap-6">
          <FormField class="flex flex-col gap-2">
            <label class="text-sm font-medium text-neutral-600" for="email"
              >Email</label
            >
            <InputText
              id="email"
              class="w-full h-12 px-4 border border-neutral-200 rounded-lg bg-white text-base text-brand-dark placeholder:text-neutral-300 focus:outline-none focus:ring-2 focus:ring-accent/20 focus:border-accent transition-all"
              placeholder="Digite seu email"
              type="email"
            />
            <!-- <Message severity="error" size="small" variant="simple">{{ $form.email.error?.message }}</Message> -->
          </FormField>
          <FormField class="flex flex-col gap-2">
            <label class="text-sm font-medium text-neutral-600" for="password"
              >Senha</label
            >
            <InputText
              id="password"
              class="w-full h-12 px-4 border border-neutral-200 rounded-lg bg-white text-base text-brand-dark placeholder:text-neutral-300 focus:outline-none focus:ring-2 focus:ring-accent/20 focus:border-accent transition-all"
              placeholder="Digite seu senha"
              type="password"
            />
            <!-- <Message severity="error" size="small" variant="simple">{{ $form.password.error?.message }}</Message> -->
          </FormField>
          <Button
            class="w-full h-12 mt-2 bg-accent text-white font-semibold rounded-lg text-base shadow-sm hover:bg-accent/90 focus:outline-none focus:ring-4 focus:ring-accent/30 transition-all active:scale-[0.98]"
            label="Entrar"
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
