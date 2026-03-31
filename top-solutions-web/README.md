# TopRecruta — Sistema de Cadastro de Usuários

Aplicação front-end desenvolvida como parte do desafio técnico da **Top Solutions**, implementando um sistema completo de cadastro de usuários com autenticação e operações de CRUD.

---

## Tecnologias utilizadas

- [Vue.js 3](https://vuejs.org/) com Composition API
- [Vue Router 4](https://router.vuejs.org/)
- [TypeScript](https://www.typescriptlang.org/)
- [Vite](https://vitejs.dev/)
- [PrimeVue 4](https://primevue.org/) + PrimeIcons
- [Tailwind CSS 4](https://tailwindcss.com/)
- [ViaCEP API](https://viacep.com.br/)
- LocalStorage + JWT (simulado para front-end) (persistência de dados e autenticação)

---

## Funcionalidades implementadas

### Autenticação
- Login com **email** e **senha** (bônus do desafio)
- Token JWT simulado gerado e armazenado no `localStorage`, com verificação de assinatura e expiração (TTL configurável via `VITE_AUTH_TOKEN_TTL_MS` no `.env`)
- Proteção de rotas via `beforeEach` no Vue Router — acesso direto por URL sem autenticação redireciona para `/login`
- Logout invalida o token e redireciona para a tela de login

### CRUD de Usuários
- **Criar** usuário com formulário validado
- **Listar** usuários ordenados do mais recente ao mais antigo
- **Editar** usuário com formulário pré-preenchido
- **Excluir** usuário com modal de confirmação — o card some imediatamente sem reload

### Integração com ViaCEP
- Preenchimento automático de Rua, Bairro, Cidade e Estado ao informar o CEP
- Tratamento de CEP inválido ou não encontrado com mensagem de erro

### Validações do formulário
- Todos os campos são obrigatórios
- Nome: limite de 100 caracteres
- Data de nascimento: máscara `DD/MM/AAAA` com validação de data real
- CEP: validado via ViaCEP API
- Função: dropdown com opções pré-definidas
- Email no login: validação do email digitado
- Senha no login: validação do tamanho mínimo da senha inserida
- Campos inválidos destacados em vermelho com mensagens de erro

### Regras de negócio
- Idade calculada automaticamente a partir da data de nascimento
- Avatar do usuário definido pelo gênero informado (masculino / feminino)
- Endereços preenchidos automaticamente pelo CEP são somente leitura

### Responsividade
- Layout adaptado para mobile e desktop
- Cards da listagem colapsam para `flex-col` em telas pequenas com rótulos inline e exibe os dados em `grid` em telas maiores que 1024px (64rem)

---

## Pré-requisitos

- [Node.js](https://nodejs.org/) v18 ou superior
- Gerenciador de pacotes: `npm`, `yarn`, `pnpm` ou `bun` (o arquivo `.lock` não está versionado — use o de sua preferência)

---

## Variáveis de ambiente

As credenciais e configurações sensíveis são lidas de um arquivo `.env` na raiz do projeto. Esse arquivo **não é versionado** — copie o exemplo e preencha com os valores desejados:

```bash
cp .env.example .env
```

O `.env.example` já está disponível no repositório com as chaves necessárias:

```env
VITE_AUTH_EMAIL=
VITE_AUTH_PASSWORD=
VITE_AUTH_SECRET=
VITE_AUTH_TOKEN_KEY=
VITE_AUTH_TOKEN_TTL_MS=
```

| Variável               	| Descrição                                                                       	| Exemplo Sugerido       	|
|------------------------	|---------------------------------------------------------------------------------	|------------------------	|
| VITE_AUTH_EMAIL        	| E-mail predefinido para realizar o login no sistema.                            	| seu@email.com          	|
| VITE_AUTH_PASSWORD     	| Senha correspondente ao e-mail de administração.                                	| SenhaQualquer@1234     	|
| VITE_AUTH_SECRET       	| Chave secreta utilizada pelo algoritmo XOR para "assinar" o token JWT simulado. 	| sua-chave-secreta-2026 	|
| VITE_AUTH_TOKEN_KEY    	| Nome da chave que será utilizada para salvar o token no localStorage.           	| auth_token             	|
| VITE_AUTH_TOKEN_TTL_MS 	| Tempo de vida do token em milissegundos (Tempo de expiração).                   	| 1500000 (25 minutos)   	|

> **Atenção:** o Vite só expõe variáveis prefixadas com `VITE_` para o código do cliente. Variáveis sem esse prefixo ficam invisíveis no bundle final.

---

## Como rodar localmente

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/toprecruta-front-end.git
cd toprecruta-front-end

# 2. Configure as variáveis de ambiente
cp .env.example .env
# Edite o .env com as credenciais desejadas

# 3. Instale as dependências (use o gerenciador de sua preferência)
npm install
# ou
yarn install
# ou
pnpm install
# ou
bun install

# 4. Inicie o servidor de desenvolvimento
npm run dev
# ou
yarn run dev
# ou
pnpm run dev
# ou
bun run dev

# 5. Acesse no navegador
http://localhost:5173
```

### Build para produção

```bash
npm run build
# ou
yarn run build
# ou
pnpm run build
# ou
bun run build
```

O output será gerado na pasta `dist/`.

---

## Como testar

### Credenciais de acesso

| Campo | Valor                |
|-------|----------------------|
| email | `VITE_AUTH_EMAIL`    | // valor inserido no arquivo .env
| Senha | `VITE_AUTH_PASSWORD` | // valor inserido no arquivo .env

### Fluxo completo

1. Acesse `http://localhost:5173` — você será redirecionado para `/login`
2. Tente acessar `/` diretamente sem autenticar — confirme o redirecionamento para `/login`
3. Faça login com as credenciais acima
4. Cadastre um novo usuário preenchendo todos os campos; informe um CEP válido e veja o endereço ser preenchido automaticamente
5. Confirme que o novo usuário aparece no topo da lista em `/`
6. Edite o usuário clicando no ícone de lápis — os campos devem estar pré-preenchidos, incluindo a data de nascimento
7. Exclua um usuário clicando no ícone de lixeira, confirme o modal e veja o card ser removido imediatamente
8. Clique em **Sair** no cabeçalho — confirme o redirecionamento para `/login` e que o acesso às rotas protegidas foi bloqueado
9. Recarregue a página durante uma sessão ativa — a sessão deve ser mantida (token ainda válido)

---

## Estrutura do projeto

```
src/
├── assets/           # Avatares e imagens estáticas
├── components/       # Header e Footer
├── router/           # Configuração de rotas e guards
├── services/         # authService, userService, viaCepService
├── types/            # Tipagens TypeScript (User, ViaCepAddress)
└── views/            # Login, Dashboard, UserForm
```

---

## Decisões técnicas relevantes

**JWT simulado sem backend**
O desafio é 100% front-end. O `authService` gera um token no formato `header.payload.signature` em base64url com assinatura XOR determinística, o que impede adulteração manual do token no `localStorage`. Em produção, a assinatura seria feita por um backend com HMAC-SHA256 real.

**Máscara de data sem dependências externas**
O `DatePicker` do PrimeVue não insere barras ao digitar. A solução adotada foi um `InputText` com função de máscara reativa (`DD/MM/AAAA`), convertida para ISO (`YYYY-MM-DD`) apenas no momento do submit.

**Parsing de data sem `new Date(isoString)`**
`new Date("2000-01-15")` interpreta a string como UTC midnight, causando deslocamento de fuso horário no navegador. A conversão é feita manualmente via regex para evitar o problema.

**Variáveis de ambiente com `.env`**
Credenciais de autenticação (`email`, `senha`, `secret` de assinatura do JWT) e configurações do token (`TOKEN_KEY`, `TOKEN_TTL_MS`) são lidas via `import.meta.env` do Vite. O arquivo `.env` não é versionado — o repositório inclui apenas o `.env.example` com as chaves sem valores, para que cada ambiente configure os seus próprios segredos sem risco de exposição.

**Grid de listagem com CSS nativo**
O layout dos cards usa `grid-template-columns` com `minmax` e larguras fixas em vez de `grid-cols-12`, garantindo alinhamento preciso entre o cabeçalho de colunas e os valores dos cards independente do conteúdo.
