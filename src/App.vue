<script setup>
import { useRoute } from "vue-router";
import { watch, computed } from "vue";
import { useI18n } from "vue-i18n";

import Header from "./components/Header.vue";
import Footer from "./components/Footer.vue";

const route = useRoute();
const { locale } = useI18n();

watch(
  locale,
  (newLocale) => {
    document.documentElement.lang = newLocale;
  },
  { immediate: true },
);

const showLayout = computed(() => route.path !== "/login");
</script>

<template>
  <Header v-if="showLayout" />
  <main
    class="flex items-center justify-center p-8 bg-neutral-50 dark:bg-neutral-900 overflow-y-auto"
  >
    <RouterView />
  </main>
  <Footer v-if="showLayout" />
</template>
