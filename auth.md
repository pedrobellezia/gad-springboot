# Auth no Insomnia

Base URL: `http://localhost:8081`

## Register
- Method: `POST`
- URL: `{{ _.base_url }}/auth/register`
- Body JSON:

```json
{
  "nome": "Pedro Teste",
  "email": "pedro.teste@example.com",
  "senha": "123456",
  "role": "CLIENTE",
  "avatar": "https://exemplo.com/avatar.png"
}
```

### Roles aceitas
- `CLIENTE`
- `REDATOR`

### Resposta
- Retorna um JWT em texto puro

## Login
- Method: `POST`
- URL: `{{ _.base_url }}/auth/login`
- Body JSON:

```json
{
  "email": "pedro.teste@example.com",
  "senha": "123456"
}
```

### Resposta
- Retorna um JWT em texto puro

