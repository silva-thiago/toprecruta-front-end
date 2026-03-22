<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

import Button from "primevue/button";

import { getUsers } from "@/services/userService";
import type { User } from "@/types/user";

const router = useRouter();

const users = ref<User[]>([]);

const loadUsers = () => {
  const data = getUsers();
  users.value = [...data].sort((a, b) => b.createdAt - a.createdAt);
};

const goToCreateUser = () => {
  router.push("/user/new");
};

const goToEditUser = (id: string) => {
  router.push(`/user/${id}/edit`);
};

const handleDeleteUser = (id: string) => {
  const confirmed = confirm(
    `Tem certeza que deseja excluir ${users.value.find((user) => user.id === id)?.name}`,
  );

  if (confirmed) {
    users.value = users.value.filter((user) => user.id !== id);
    loadUsers();
  }
};

onMounted(() => {
  loadUsers();
});
</script>

<template>
  <section class="container min-h-svh py-8">
    <div class="mb-8 border-b border-border-strong flex justify-between pb-4">
      <h1 class="text-2xl font-bold text-text-h">Usuários</h1>
      <Button
        @click="goToCreateUser"
        label="Adicionar novo usuário"
        severity="primary"
        class="rounded sm:px-2.5 px-10 h-11 text-neutral-50"
      />
    </div>

    <ul v-if="users.length > 0" class="users-list">
      <li v-for="user in users" :key="user.id">
        <p>{{ user.name }}</p>
        <span>{{ user.age }}</span>
        <span>{{ user.role }}</span>
        <Button
          @click="goToEditUser(user.id)"
          icon="pi pi-pencil"
          label="Editar"
          severity="primary"
        />
        <Button
          @click="handleDeleteUser(user.id)"
          icon="pi pi-trash"
          label="Excluir"
          severity="danger"
        />
      </li>
    </ul>

    <p v-else>Nenhum usuário cadastrado ainda.</p>
  </section>
</template>
