<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const logout = () => {
  localStorage.removeItem("auth");
  router.push("/login");
};
</script>

<template>
  <div>
    <h1>Dashboard</h1>
    <button v-on:click="logout">Logout</button>
  </div>
</template>
