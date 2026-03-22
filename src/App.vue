<script setup>
import { useRoute } from "vue-router";
import { computed } from "vue";

import Header from "./components/Header.vue";
import Footer from "./components/Footer.vue";

const route = useRoute();

const showLayout = computed(() => route.path !== "/login");
</script>

<template>
  <Header v-if="showLayout" />
  <main
    class="flex items-center justify-center p-8 transition-colors overflow-y-auto"
  >
    <RouterView />
  </main>
  <Footer v-if="showLayout" />
</template>
