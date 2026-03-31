import type { ViaCepAddress } from "@/types/viaCep";

export const sanitizeZipcode = (value: string): string =>
  value.replace(/\D/g, "");

export const isValidZipcode = (zipcode: string): boolean =>
  /^\d{8}$/.test(sanitizeZipcode(zipcode));

export const fetchAddressByZipcode = async (
  zipcode: string,
): Promise<ViaCepAddress> => {
  const cleanZipcode = sanitizeZipcode(zipcode);

  if (!isValidZipcode(cleanZipcode)) {
    throw new Error("Formato de CEP inválido.");
  }

  const response = await fetch(
    `https://viacep.com.br/ws/${cleanZipcode}/json/`,
  );

  if (!response.ok) {
    throw new Error("Erro ao consultar o ViaCEP.");
  }

  const data = (await response.json()) as ViaCepAddress & { erro?: boolean };

  if (data.erro) {
    throw new Error("CEP não encontrado.");
  }

  return data;
};
