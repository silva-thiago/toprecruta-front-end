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
- Token JWT simulado gerado e armazenado no `localStorage`, com verificação de assinatura e expiração (TTL de 25 minutos)
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
- CPF no login: validação dos dois dígitos verificadores
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

## Como rodar localmente

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/toprecruta-front-end.git
cd toprecruta-front-end

# 2. Instale as dependências (use o gerenciador de sua preferência)
npm install
# ou
yarn install
# ou
pnpm install
# ou
bun install

# 3. Inicie o servidor de desenvolvimento
npm run dev
# ou
yarn run dev
# ou
pnpm run dev
# ou
bun run dev

# 4. Acesse no navegador
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

| Campo | Valor                   |
|-------|-------------------------|
| email | admin@topsolutions.com  |
| Senha | Admin@1234              |

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

**Grid de listagem com CSS nativo**
O layout dos cards usa `grid-template-columns` com `minmax` e larguras fixas em vez de `grid-cols-12`, garantindo alinhamento preciso entre o cabeçalho de colunas e os valores dos cards independente do conteúdo.
