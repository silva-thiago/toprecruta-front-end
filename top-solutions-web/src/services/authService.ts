/**
 * authService.ts
 *
 * JWT simulado 100% no front-end, sem dependências externas.
 * Credenciais e chave de assinatura lidas do arquivo .env via import.meta.env.
 *
 * Estrutura do token: header.payload.signature
 * - header    : { alg: "HS256", typ: "JWT" } em base64url
 * - payload   : { sub: email, iat, exp } em base64url
 * - signature : assinatura XOR determinística com VITE_AUTH_SECRET
 *
 * ⚠️  Em produção, NUNCA assine tokens sem um backend.
 *     Aqui o objetivo é demonstrar o fluxo JWT no escopo
 *     de um desafio front-end com autenticação local.
 */

// ─── Variáveis de ambiente ────────────────────────────────────────────────────
const VALID_EMAIL = import.meta.env.VITE_AUTH_EMAIL as string;
const VALID_PASSWORD = import.meta.env.VITE_AUTH_PASSWORD as string;
const SECRET = import.meta.env.VITE_AUTH_SECRET as string;
const TOKEN_KEY = import.meta.env.VITE_AUTH_TOKEN_KEY as string;
const TOKEN_TTL_MS = Number(import.meta.env.VITE_AUTH_TOKEN_TTL_MS);

// ─── Helpers base64url ────────────────────────────────────────────────────────
const toBase64url = (str: string): string =>
  btoa(unescape(encodeURIComponent(str)))
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/, "");

const fromBase64url = (str: string): string => {
  const padded = str.replace(/-/g, "+").replace(/_/g, "/");
  const pad = (4 - (padded.length % 4)) % 4;

  return decodeURIComponent(escape(atob(padded + "=".repeat(pad))));
};

// ─── Assinatura determinística (XOR com SECRET) ───────────────────────────────
const sign = (data: string): string => {
  let result = "";

  for (let i = 0; i < data.length; i++) {
    result += String.fromCharCode(
      data.charCodeAt(i) ^ SECRET.charCodeAt(i % SECRET.length),
    );
  }

  return toBase64url(result);
};

// ─── Geração do token ─────────────────────────────────────────────────────────
const generateToken = (email: string): string => {
  const header = toBase64url(JSON.stringify({ alg: "HS256", typ: "JWT" }));
  const payload = toBase64url(
    JSON.stringify({
      sub: email,
      iat: Date.now(),
      exp: Date.now() + TOKEN_TTL_MS,
    }),
  );
  const signature = sign(`${header}.${payload}`);

  return `${header}.${payload}.${signature}`;
};

// ─── Validação do token ───────────────────────────────────────────────────────
export const validateToken = (): boolean => {
  try {
    const token = localStorage.getItem(TOKEN_KEY);

    if (!token) return false;

    const parts = token.split(".");

    if (parts.length !== 3) return false;

    const [header, payload, signature] = parts;

    // Verifica integridade: re-assina e compara com o token recebido
    if (sign(`${header}.${payload}`) !== signature) return false;

    // Verifica expiração
    const { exp } = JSON.parse(fromBase64url(payload)) as { exp: number };

    if (Date.now() > exp) {
      localStorage.removeItem(TOKEN_KEY);

      return false;
    }

    return true;
  } catch {
    return false;
  }
};

// ─── Login ────────────────────────────────────────────────────────────────────
export type LoginResult =
  | { success: true }
  | { success: false; message: string };

export const login = (email: string, password: string): LoginResult => {
  if (email !== VALID_EMAIL || password !== VALID_PASSWORD) {
    return { success: false, message: "Email ou senha incorretos." };
  }

  localStorage.setItem(TOKEN_KEY, generateToken(email));
  localStorage.removeItem("auth"); // remove flag legada

  return { success: true };
};

// ─── Logout ───────────────────────────────────────────────────────────────────
export const logout = (): void => {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem("auth");
};
