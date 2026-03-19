<script setup lang="ts">
import { useRouter } from "vue-router";

import Button from "primevue/button";

const router = useRouter();

const login = () => {
  localStorage.setItem("auth", "true");
  router.push("/");
};
</script>

<template>
  <div class="card flex justify-center">
    <h1>Usuários</h1>
    <p>Painel Administrativo</p>
    <form class="flex flex-col gap-4 w-full sm:w-56">
      <div class="flex flex-col gap-1">
        <label for="usermail">Email</label>
        <input type="text" placeholder="Digite seu email" />
      </div>
      <div class="flex flex-col gap-1">
        <label for="password">Senha</label>
        <input type="password" placeholder="Digite seu senha" />
      </div>
      <Button
        label="Entrar"
        severity="secondary"
        type="submit"
        variant="text"
        v-on:click="login"
      />
    </form>
  </div>
</template>

<style>
.app-login-content {
  display: grid;
  place-items: center;
}

.app-login-form {
  display: grid;
  place-items: center;
}
</style>
