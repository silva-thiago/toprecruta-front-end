<script setup>
import { RouterLink, RouterView } from "vue-router";
</script>

<template>
  <p><strong>Current route path:</strong> {{ $route.fullPath }}</p>
  <nav>
    <RouterLink to="/login">Login</RouterLink>
    <RouterLink to="/">Dashboard</RouterLink>
  </nav>
  <main>
    <RouterView />
  </main>
</template>
