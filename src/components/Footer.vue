<script setup lang="ts">
const startYear = 2024;
const currentYear = new Date().getFullYear();
const displayYears = startYear
  ? `${startYear} - ${currentYear}`
  : `${currentYear}`;
</script>

<template>
  <footer class="h-28 bg-brand-muted flex items-center justify-center px-6">
    <p class="text-white text-sm text-center font-medium opacity-90">
      &copy; {{ displayYears }} TopRecruta&trade;. Todos os direitos reservados.
    </p>
  </footer>
</template>
