<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";

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
  // abrir modal de confirmação
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
  <Header />
  <main class="main">
    <section>
      <div class="flex">
        <h1>Usuários</h1>
        <Button
          @click="goToCreateUser"
          label="Adicionar novo usuário"
          severity="primary"
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
  </main>
  <Footer />
</template>

<style scoped>
.main {
  display: grid;
  place-items: center;
}
</style>
