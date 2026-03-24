import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/Login.vue";
import DashboardView from "@/views/Dashboard.vue";
import UserFormView from "@/views/UserForm.vue";

import { validateToken } from "@/services/authService";

const routes = [
  { path: "/login", name: "login", component: LoginView },
  {
    path: "/",
    name: "dashboard",
    component: DashboardView,
    meta: { requiresAuth: true },
  },
  {
    path: "/user/new",
    name: "user-create",
    component: UserFormView,
    meta: { requiresAuth: true },
  },
  {
    path: "/user/:id/edit",
    name: "user-edit",
    component: UserFormView,
    meta: { requiresAuth: true },
  },
  { path: "/:pathMatch(.*)*", redirect: "/" },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, _from, next) => {
  // Valida o token JWT — verifica assinatura e expiração
  const isAuthenticated = validateToken();

  // Rota protegida sem token válido → login
  if (to.meta.requiresAuth && !isAuthenticated) {
    return next("/login");
  }

  // Já autenticado tentando acessar /login → home
  if (to.name === "/login" && isAuthenticated) {
    return next("/");
  }

  next();
});

export default router;
