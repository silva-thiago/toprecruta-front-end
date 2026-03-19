import type { User } from "@/types/user";

const STORAGE_KEY = "users";

export const createUser = (user: User): void => {
  try {
    const users = getUsers();
    users.push(user);
    saveUser(users);
  } catch (error) {
    console.error("Erro ao tentar criar o usuário:", error);
  }
};

export const saveUser = (users: User[]): void => {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(users));
  } catch (error) {
    console.error("Erro ao tentar salvar o usuário:", error);
  }
};

export const getUsers = (): User[] => {
  try {
    const data = localStorage.getItem(STORAGE_KEY);
    return data ? JSON.parse(data) : [];
  } catch (error) {
    console.error("Erro ao tentar obter os usuários:", error);
    return [];
  }
};

export const getUserById = (id: string): User | undefined => {
  const users = getUsers();
  return users.find((user) => user.id === id);
};

export const updateUser = (updateUser: User): void => {
  try {
    const users = getUsers();
    const updateUsers = users.map((user) =>
      user.id === updateUser.id ? updateUser : user,
    );
    saveUser(updateUsers);
  } catch (error) {
    console.error("Erro ao tentar atualizar o usuário:", error);
  }
};

export const deleteUser = (id: string): void => {
  try {
    const users = getUsers();
    const filteredUsers = users.filter((user) => user.id !== id);
    saveUser(filteredUsers);
  } catch (error) {
    console.error("Erro ao tentar deletar o usuário:", error);
    return undefined;
  }
};
