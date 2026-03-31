<script setup lang="ts">
import { useRouter } from "vue-router";

import Button from "primevue/button";

const router = useRouter();

const logout = () => {
  localStorage.removeItem("auth");
  router.push("/login");
};
</script>

<template>
  <header
    class="h-28 bg-surface-soft dark:bg-neutral-900 border-b border-neutral-100 px-6 flex justify-center items-center"
  >
    <nav
      class="container flex justify-between items-center sm:gap-4 md:gap-6 lg:gap-12 max-w-6xl"
    >
      <RouterLink
        to="/"
        class="flex items-center gap-2 text-brand-dark dark:text-white font-semibold hover:text-accent transition-colors"
        ><i class="pi pi-users text-lg"></i><span>Usuários</span></RouterLink
      >
      <RouterLink
        to="/user/new"
        class="flex items-center gap-2 text-brand-dark dark:text-white font-semibold hover:text-accent transition-colors"
        ><i class="pi pi-plus-circle text-lg"></i
        ><span>Adicionar</span> usuário</RouterLink
      >
      <Button
        @click="logout"
        class="flex items-center gap-2 text-danger font-bold hover:opacity-80 transition-opacity"
        icon="pi pi-sign-out"
        label="Sair"
        severity="danger"
        variant="link"
      />
    </nav>
  </header>
</template>
