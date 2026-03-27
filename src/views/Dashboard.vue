<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";

import Dialog from "primevue/dialog";
import Card from "primevue/card";
import Avatar from "primevue/avatar";
import Button from "primevue/button";
import Paginator from "primevue/paginator";

import maleAvatar from "@/assets/avatar-male.png";
import femaleAvatar from "@/assets/avatar-female.png";

import { getUsers, deleteUser } from "@/services/userService";
import type { User } from "@/types/user";

const router = useRouter();

const users = ref<User[]>([]);
const showDeleteDialog = ref(false);
const selectedUser = ref<User | null>(null);

const first = ref(0);
const rows = ref(5);

const totalRecords = computed(() => users.value.length);

const paginatedUsers = computed(() => {
  return users.value.slice(first.value, first.value + rows.value);
});

const loadUsers = () => {
  const data = getUsers();
  users.value = [...data].sort((a, b) => b.createdAt - a.createdAt);

  if (first.value >= users.value.length && users.value.length > 0) {
    first.value = Math.max(
      0,
      Math.floor((users.value.length - 1) / rows.value) * rows.value,
    );
  }

  if (!users.value.length) {
    first.value = 0;
  }
};

const onPageChange = (event: { first: number; rows: number }) => {
  first.value = event.first;
  rows.value = event.rows;
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
  loadUsers();
  closeDeleteDialog();
};

const formatZipcode = (zipcode: string) => {
  if (!zipcode) return "";

  const cleanedZipcode = zipcode.replace(/\D/g, "");

  return cleanedZipcode.replace(/(\d{5})(\d{3})/, "$1-$2");
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
          class: 'w-8 h-8',
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

    <div
      class="flex justify-between gap-4 border-b border-border-strong mb-4 pb-6"
    >
      <h1 class="text-2xl font-bold text-text-h">Usuários</h1>
      <Button
        @click="goToCreateUser"
        label="Adicionar novo usuário"
        severity="primary"
        class="rounded sm:px-2.5 px-10 h-11 text-neutral-50"
      />
    </div>

    <div v-if="users.length" class="hidden lg:grid lg:grid-cols-12 gap-4 px-8">
      <span />
      <span>Nome</span>
      <span class="col-start-3">Idade</span>
      <span class="col-start-4">Função</span>
      <span class="col-start-6">Endereço</span>
      <span class="col-start-11 col-span-2">Adicionado em:</span>
      <span />
    </div>

    <div v-if="users.length" class="flex flex-col gap-4">
      <Card
        v-for="user in paginatedUsers"
        :key="user.id"
        :pt="{
          root: { class: 'bg-surface-soft rounded-2xl shadow-sm p-0' },
          content: {
            class: 'flex items-center lg:grid lg:grid-cols-12 gap-4',
          },
        }"
      >
        <template #content>
          <div class="flex justify-center">
            <Avatar
              size="xlarge"
              shape="circle"
              :image="getAvatarByGender(user.gender)"
              :pt="{ root: { class: 'size-20 rounded-full' } }"
            />
          </div>

          <div
            class="flex flex-col flex-10 justify-center lg:items-center lg:grid lg:grid-cols-10 lg:col-span-10 lg:col-start-2 gap-y-2"
          >
            <h2 class="font-bold">
              {{ user.name }}
            </h2>

            <p class="col-start-2">{{ user.age }} anos</p>

            <p class="col-start-3">{{ user.role }}</p>

            <p class="col-start-5 col-span-5">
              {{ user.street }}, {{ user.neighborhood }}, {{ user.city }}/{{
                user.state
              }}<br />
              CEP: {{ user.zipcode }}
            </p>

            <p class="col-start-10">
              {{ formatCreatedAt(user.createdAt) }}
            </p>
          </div>

          <div class="flex flex-col justify-center lg:flex-row lg:col-start-12">
            <Button
              @click="goToEditUser(user.id)"
              aria-label="Editar"
              icon="pi pi-pen-to-square"
              severity="text"
              rounded
              :pt="{
                root: {
                  class:
                    'text-accent hover:bg-accent/10 transition-colors rounded-full',
                },
              }"
            />

            <Button
              @click="openDeleteDialog(user)"
              aria-label="Excluir"
              icon="pi pi-trash"
              severity="text"
              rounded
              :pt="{
                root: {
                  class:
                    'text-accent hover:bg-danger/10 hover:text-danger transition-colors rounded-full',
                },
              }"
            />
          </div>
        </template>
      </Card>

      <Paginator
        @page="onPageChange"
        v-if="users.length > 5"
        :pt="{
          root: {
            class:
              'mt-4 rounded-2xl border border-surface-200 dark:border-neutral-800 bg-white dark:bg-neutral-900 px-4 py-3',
          },
        }"
        :first="first"
        :rows="rows"
        :totalRecords="totalRecords"
        :rowsPerPageOptions="[5, 10, 20]"
        :template="{
          '640px': 'PrevPageLink CurrentPageReport NextPageLink',
          '960px':
            'FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink',
          default:
            'FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown',
        }"
        currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords}"
      />
    </div>

    <p v-else class="mb-4">Nenhum usuário cadastrado ainda.</p>
  </section>
</template>

<style scoped>
:deep(.p-card) {
  padding: 2rem;
}
</style>
