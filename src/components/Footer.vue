<script setup lang="ts">
const startYear = 2024;
const currentYear = new Date().getFullYear();
const displayYears = startYear
  ? `${startYear} - ${currentYear}`
  : `${currentYear}`;
</script>

<template>
  <footer class="app-footer">
    <p>
      &copy; {{ displayYears }} TopRecruta&trade;. Todos os direitos reservados.
    </p>
  </footer>
</template>

<style scoped>
/* Footer base */
.app-footer {
  align-items: center;
  background: var(--brand-muted);
  color: var(--text-inverse);
  display: flex;
  justify-content: center;
  min-height: var(--footer-height);
  padding: 16px;
  text-align: center;
}

.app-footer p {
  font-size: 14px;
  font-weight: 400;
  line-height: 1.55;
}
</style>
