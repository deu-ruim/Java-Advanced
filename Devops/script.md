# Devops Tools & Cloud Computing

# Deu Ruim

## Dados da VM

**Grupo de Recursos** : rg-deuruim-gs
**Nome da VM** : deuruim-gs
**User Admin** : gserick
**IP da VM** : 74.163.240.166
**Senha da VM** : Erick@010506

# Importante

No vídeo utilizamos o deploy na VM gratuita porem por conta de pouco espaço fizemos o deploy em uma VM paga, não gravamos novamente pois o processo era o mesmo.

---

## Criação do grupo de recursos

az group create --name rg-deuruim-gs --location brazilsouth

## Criação do grupo da VM

az vm create --resource-group rg-deuruim-gs --name deuruim-gs --image Canonical:0001-com-ubuntu-server-focal:20_04-lts:latest --size Standard_B2s --admin-username gserick --authentication-type password --admin-password Erick@010506

---

## Build e run do Banco de Dados

**Build** : docker build -f DbDocker -t postgres-db-gs .
**Run** : docker run -d --name postgres-container-deu-ruim -p 5432:5432 -v postgres-dados:/var/lib/postgresql/data --restart always postgres-db-gs


### Comando para entrar e sair no container do Database

**Para entrar** : docker exec -it postgres-container-deu-ruim psql -U admin -d dbDeuRuim

**Para Sair** : \q 

---

## Build e run de Java

**Build** : docker build -t java-app-gs -f JavaDocker .
**Run** : docker run -d --name java-container-deu-ruim --restart=always -p 8080:8080 java-app-gs