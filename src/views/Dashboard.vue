<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";

import Dialog from "primevue/dialog";
import Card from "primevue/card";
import Avatar from "primevue/avatar";
import Button from "primevue/button";

import maleAvatar from "@/assets/avatar-male.png";
import femaleAvatar from "@/assets/avatar-female.png";

import { getUsers, deleteUser } from "@/services/userService";
import type { User } from "@/types/user";

const router = useRouter();

const users = ref<User[]>([]);
const showDeleteDialog = ref(false);
const selectedUser = ref<User | null>(null);

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

const openDeleteDialog = (user: User) => {
  selectedUser.value = user;
  showDeleteDialog.value = true;
};

const closeDeleteDialog = () => {
  selectedUser.value = null;
  showDeleteDialog.value = false;
};

const confirmDeleteUser = () => {
  if (!selectedUser.value) return;

  deleteUser(selectedUser.value.id);
  users.value = users.value.filter(
    (user) => user.id !== selectedUser.value?.id,
  );

  closeDeleteDialog();
};

const formatCreatedAt = (timestamp: number) => {
  return new Date(timestamp).toLocaleDateString("pt-BR");
};

const getAvatarByGender = (gender: string) => {
  return gender === "Feminino" ? femaleAvatar : maleAvatar;
};

onMounted(() => {
  loadUsers();
});
</script>

<template>
  <section class="container min-h-svh py-8">
    <Dialog
      @hide="closeDeleteDialog"
      v-model:visible="showDeleteDialog"
      :style="{ width: '32rem', maxWidth: '95vw' }"
      :closable="true"
      modal
      :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
      :pt="{
        root: {
          class: 'rounded-2xl overflow-hidden bg-surface shadow-2xl',
        },
        mask: {
          class: 'bg-white/40! backdrop-blur-[2px]',
        },
        header: {
          class: 'flex justify-end px-8 pt-7 pb-4',
        },
        closeButton: {
          class: 'w-8 h-8 rounded-full',
        },
        closeIcon: {
          class: 'text-xs',
        },
        content: {
          class: 'px-8 py-2',
        },
        footer: {
          class: 'flex justify-center gap-4 px-8 pb-7 pt-4',
        },
      }"
    >
      <div>
        <h1 class="text-center">Excluir usuário</h1>
        <p v-if="selectedUser" class="text-center text-base text-muted">
          Tem certeza que deseja excluir o usuário
          <strong>{{ selectedUser.name }}</strong
          >?
        </p>
      </div>

      <template #footer>
        <div class="flex justify-center gap-4">
          <Button
            @click="closeDeleteDialog"
            label="Cancelar"
            severity="primary"
            variant="outlined"
            class="bg-transparent! border border-accent! text-accent! hover:bg-brand-muted! hover:text-surface! px-10 h-11 rounded"
            type="button"
          />
          <Button
            @click="confirmDeleteUser"
            label="Excluir"
            severity="primary"
            class="bg-danger! px-10 h-11 rounded"
          />
        </div>
      </template>
    </Dialog>

    <div class="mb-8 border-b border-border-strong flex justify-between pb-4">
      <h1 class="text-2xl font-bold text-text-h">Usuários</h1>
      <Button
        @click="goToCreateUser"
        label="Adicionar novo usuário"
        severity="primary"
        class="rounded sm:px-2.5 px-10 h-11 text-neutral-50"
      />
    </div>

    <div v-if="users.length" class="flex flex-col gap-4">
      <Card
        v-for="user in users"
        :key="user.id"
        class="flex flex-col bg-surface-soft p-10 rounded-2xl shadow-sm gap-12"
      >
        <template #content>
          <div class="flex items-center justify-between">
            <Avatar
              :image="getAvatarByGender(user.gender)"
              size="xlarge"
              shape="circle"
            />
            <h2>{{ user.name }}</h2>

            <div class="flex items-center justify-between gap-4">
              <p>{{ user.age }} anos</p>
              <p>{{ user.role }}</p>
              <p>
                {{ user.street }}, {{ user.neighborhood }}, {{ user.city }}/{{
                  user.state
                }}
              </p>
              <p>CEP: {{ user.zipcode }}</p>
            </div>
            <p>
              {{ formatCreatedAt(user.createdAt) }}
            </p>

            <Button
              @click="goToEditUser(user.id)"
              aria-label="Editar"
              class="text-accent"
              icon="pi pi-pencil"
              severity="text"
              rounded
            />
            <Button
              @click="openDeleteDialog(user)"
              aria-label="Excluir"
              class="text-accent"
              icon="pi pi-trash"
              severity="text"
              rounded
            />
          </div>
        </template>
      </Card>
    </div>

    <p v-else class="mb-4">Nenhum usuário cadastrado ainda.</p>
  </section>
</template>

<style scoped>
:deep(.p-card) {
  padding: 2rem;
}
</style>
