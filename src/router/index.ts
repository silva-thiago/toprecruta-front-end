import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/Login.vue";
import DashboardView from "@/views/Dashboard.vue";
import UserFormView from "@/views/UserForm.vue";

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
  const isAuthenticated = localStorage.getItem("auth") === "true";

  // Usuário não autenticado → bloqueado
  if (to.meta.requiresAuth && !isAuthenticated) {
    return next("/login");
  }
  // Usuário autenticado, mas tentando acessar /login → redirecionado para /
  if (to.name === "/login" && isAuthenticated) {
    return next("/");
  }

  next();
});

export default router;
