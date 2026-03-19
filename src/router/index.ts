import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/Login.vue";
import DashboardView from "@/views/Dashboard.vue";
import UserFormView from "@/views/UserForm.vue";

const routes = [
  { path: "/login", component: LoginView },
  { path: "/", component: DashboardView, meta: { requiresAuth: true } },
  {
    path: "/user/new",
    name: "user-create",
    component: UserFormView,
    meta: { requiresAuth: true },
  },
  {
    path: "/user/:id",
    name: "user-edit",
    component: UserFormView,
    meta: { requiresAuth: true },
  },
  { path: "/:pathMatch(.*)", redirect: "/" },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem("auth") === "true";

  // Usuário não autenticado → bloqueado
  if (to.meta.requiresAuth && !isAuthenticated) {
    return next("/login");
  }
  // Usuário autenticado, mas tentando acessar /login → redirecionado para /
  if (to.path === "/login" && isAuthenticated) {
    return next("/");
  }

  next();
});

export default router;
