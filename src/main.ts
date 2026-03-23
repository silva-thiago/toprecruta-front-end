import { createApp } from "vue";

import App from "@/App.vue";
import router from "@/router";
import i18n from "@/i18n";

import PrimeVue from "primevue/config";
import Aura from "@primeuix/themes/aura";
import "primeicons/primeicons.css";

import "@/style.css";

createApp(App)
  .use(router)
  .use(i18n)
  .use(PrimeVue, {
    ripple: true,
    inputVariant: "filled",
    theme: {
      preset: Aura,
      options: {
        darkModeSelector: "system",
        cssLayer: true,
      },
    },
    zIndex: {
      modal: 1100,
      overlay: 1000,
      menu: 1000,
      tooltip: 1100,
    },
  })
  .mount("#app");
