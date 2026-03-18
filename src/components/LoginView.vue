<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const login = () => {
  localStorage.setItem("auth", "true");
  router.push("/");
};
</script>

<template>
  <form>
    <input type="text" placeholder="Username" />
    <input type="password" placeholder="Password" />
    <button v-on:click="login" type="submit">Login</button>
  </form>
</template>
