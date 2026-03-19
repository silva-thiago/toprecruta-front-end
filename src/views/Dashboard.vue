<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

import { getUsers } from "@/services/userService";
import type { User } from "@/types/user";

const router = useRouter();
const users = ref<User[]>([]);

const logout = () => {
  localStorage.removeItem("auth");
  router.push("/login");
};

onMounted(() => {
  const data = getUsers();
  users.value = [...data].sort((a, b) => b.createdAt - a.createdAt);
});
</script>

<template>
  <div>
    <h1>Dashboard</h1>
    <button v-on:click="logout">Logout</button>

    <h2>Usuários</h2>
    <ul class="users-list">
      <li v-for="user in users" :key="user.id">
        {{ user.name }} - {{ user.age }} anos.
      </li>
    </ul>
  </div>
</template>
