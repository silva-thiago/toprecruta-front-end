<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";

import { createUser } from "@/services/userService";
import type { User } from "@/types/user";

const router = useRouter();

const name = ref("");
const birthdate = ref("");
const gender = ref<"M" | "F">("M");
const role = ref("");
const zipcode = ref("");

const calculateAge = (date: string): number => {
  const today = new Date();
  const birth = new Date(date);
  let age = today.getFullYear() - birth.getFullYear();
  const month = today.getMonth() - birth.getMonth();

  if (month < 0 || (month === 0 && today.getDate() < birth.getDate())) {
    age--;
  }

  return age;
};

const handleSubmit = () => {
  const user: User = {
    id: Date.now().toString(),
    name: name.value,
    birthdate: birthdate.value,
    age: calculateAge(birthdate.value),
    gender: gender.value,
    role: role.value,
    zipcode: zipcode.value,
    street: "",
    neighborhood: "",
    city: "",
    state: "",
    createdAt: Date.now(),
  };

  createUser(user);
  router.push("/");
};
</script>
