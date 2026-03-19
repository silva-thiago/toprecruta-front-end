<script setup lang="ts">
import { useRouter } from "vue-router";

import Button from "primevue/button";

const router = useRouter();

const goToDashboard = () => {
  router.push("/");
};

const goToCreateUser = () => {
  router.push("/user/new");
};

const logout = () => {
  localStorage.removeItem("auth");
  router.push("/login");
};
</script>

<template>
  <header class="app-header">
    <div class="app-header__content">
      <Button
        @click="goToDashboard"
        class="nav-btn"
        icon="pi pi-users"
        label="Usuários"
        severity="primary"
        variant="link"
      />
      <Button
        @click="goToCreateUser"
        class="nav-btn"
        icon="pi pi-plus-circle"
        label="Adicionar usuário"
        severity="primary"
        variant="link"
      />
      <Button
        @click="logout"
        class="nav-btn nav-btn-danger"
        icon="pi pi-sign-out"
        label="Sair"
        severity="danger"
        variant="link"
      />
    </div>
  </header>
</template>

<style scoped>
/* Header base */
.app-header {
  min-height: var(--header-height);
  background: var(--surface);
  border-bottom: 1px solid var(--border);
}

.app-header__content {
  width: min(100% - 64px, var(--container));
  min-height: var(--header-height);
  margin-inline: auto;
  display: flex;
  align-items: center;
  justify-content: space-around;
  gap: 24px;
}

.nav-btn {
  background: transparent;
  border: 0;
  color: var(--brand-dark);
  cursor: pointer;
  font: 16px/1 var(--sans);
  font-weight: 600;
}

.nav-btn-danger {
  color: var(--danger);
}

@media (max-width: 768px) {
  .app-header {
    min-height: 80px;
  }

  .app-header__content {
    gap: 12px;
    justify-content: space-between;
    min-height: 80px;
    padding: 0 12px;
    width: min(100% - 30px, var(--container));
  }

  .nav-btn {
    font-size: 12px;
  }
}
</style>
