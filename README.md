# DR4_TP1

## 1. Explique de forma sucinta o que são microsserviços.

**Resposta:** Microsserviços são pequenos serviços de software separados, cada um responsável por uma parte específica do negócio. Eles podem ser desenvolvidos, implantados e atualizados de forma independente e se comunicam normalmente por APIs ou mensagens.

## 2. Cite uma vantagem da arquitetura de microsserviços.

**Resposta:** A capacidade de implantação independente melhora a escala e a robustez dos sistemas, além de permitir o uso de tecnologias heterogêneas e agnósticas. Ela também reduz o risco de atualizações e permite que as equipes de desenvolvimento trabalhem em paralelo de maneira mais eficiente, sem que os desenvolvedores interfiram diretamente no trabalho uns dos outros

## 3. Cite uma desvantagem da arquitetura de microsserviços.

**Resposta:** A comunicação via redes aumenta a latência do projeto, e acontecimentos como falhas parciais também impactam no aumento da complexidade operacional. Como existem vários serviços separados, é necessário controlar comunicação, monitoramento, falhas de rede e implantação de cada serviço.

## 4. Cite uma característica dos microsserviços que os diferenciam de outras arquiteturas de software.

**Resposta:** Uma característica importante é a implantação independente, o encapsulamento estrito dos dados, cada microsserviço pode ter seu próprio ciclo de desenvolvimento e ser colocado em produção sem exigir a implantação dos demais.

## 5. Explique de forma sucinta o que é um monólito.

**Resposta:** Um monólito é um sistema de software estruturado e empacotado como uma única unidade de implantação, onde toda a sua funcionalidade precisa ser implantada em conjunto.

## 6. Explique de forma sucinta o que significa Acoplamento do ponto de vista de Engenharia de Software.

**Resposta:** Acoplamento é o nível de dependência entre partes do sistema. Quanto maior o acoplamento, maior a chance de uma alteração em um módulo exigir mudanças em outros módulos.

## 7. Explique de forma sucinta o que significa Coesão do ponto de vista de Engenharia de Software.

**Resposta:** Coesão indica o quanto os elementos de um módulo estão relacionados no mesmo sistema. Uma arquitetura orientada ao domínio de negócios busca alta coesão funcional de negócio, seguindo a diretriz de que "o código que muda junto pelas mesmas regras de negócio deve permanecer junto".

## 8. Explique de forma sucinta o que é um Agregado do DDD.

**Resposta:** Um Agregado é um conjunto de objetos do domínio que deve ser tratado como uma única unidade de consistência. Ele possui uma raiz, chamada Aggregate Root, que controla as operações e protege as regras do conjunto.

## 9. Cite uma vantagem de construir microsserviços usando Agregados do DDD.

**Resposta:** Os agregados eliminam referências diretas de objetos através dos limites dos microsserviços (utilizando em vez disso referências baseadas exclusivamente em IDs/chaves primárias), o que garante que qualquer transação ACID fique isolada no banco de dados de um único serviço.

## 10. Dê um exemplo de mapeamento de um Contexto Delimitado para um microserviço, utilizando a linguagem Java.

**Resposta:** Um Contexto Delimitado pode virar um serviço responsável apenas por aquele domínio. No exemplo abaixo, o contexto de Usuário fica isolado em um microserviço próprio, sem colocar regras de Estoque ou Pagamento na mesma classe.

```java
package ecommerce.usuario;

// Exemplo simples de um contexto delimitado de Usuário.
public class UsuarioService {

    public Usuario buscarUsuario(Long id) {
        // Regras e dados pertencentes ao contexto de Usuário.
        return new Usuario(id, "Cliente");
    }
}

record Usuario(Long id, String nome) {}
```

## 11. Dê um exemplo de mapeamento de um Agregado, com Objeto de Valor, para o contexto de um microserviço, utilizando a linguagem Java.

**Resposta:** Neste exemplo, Pagamento é a raiz do Agregado e Dinheiro é um Objeto de Valor. As regras do pagamento ficam concentradas nesse domínio, evitando que outros módulos alterem diretamente seu estado.

```java
package ecommerce.pagamento;

import java.math.BigDecimal;

// Objeto de Valor: representa um valor monetário.
record Dinheiro(BigDecimal valor, String moeda) {

    Dinheiro {
        if (valor.signum() < 0) {
            throw new IllegalArgumentException("Valor inválido");
        }
    }
}

// Aggregate Root do pagamento.
public class Pagamento {

    private final Long id;
    private final Dinheiro total;
    private String status;

    public Pagamento(Long id, Dinheiro total) {
        this.id = id;
        this.total = total;
        this.status = "PENDENTE";
    }

    public void aprovar() {
        this.status = "APROVADO";
    }
}
```

## 12. Porque o compartilhamento de banco de dados é uma estratégia de integração ruim sob o ponto de vista de microsserviços?

**Resposta:** O compartilhamento direto de banco de dados viola o princípio do acoplamento fraco e impede a implantação independente. Ele gera um tipo de acoplamento de implementação em que o serviço externo fica dependente do esquema físico das tabelas, tipos de dados e nomes de colunas do banco. Se o microsserviço proprietário das informações precisar mudar sua arquitetura interna de tabelas ou renomear colunas para atender a novos requisitos, ele quebrará as outras aplicações que leem ou gravam diretamente nele. Em vez de expor o armazenamento de dados, o serviço deve esconder sua persistência e usar o conceito de ocultação de informações, estabelecendo interfaces e contratos de APIs públicos e estáveis.

# Parte 2 - Estudo de Caso e Refatoração Prática

**Cenário:** Você assumiu a liderança técnica de um sistema monolítico de E-commerce legado. Atualmente, os módulos de Usuário, Estoque e Pagamento dividem o mesmo banco de dados e possuem classes fortemente acopladas, o que tem gerado travamentos no sistema durante picos de acesso. A diretoria aprovou a migração gradual para microsserviços.

## 13. Explique, com suas próprias palavras, qual estratégia de migração (como o padrão Strangler Fig / Padrão Estrangulador ou Branch by Abstraction) você utilizaria para iniciar a transformação desse monólito em microsserviços de forma segura, sem precisar desligar o sistema atual.

**Resposta:** Eu utilizaria o padrão Strangler Fig. Em vez de reescrever todo o sistema de uma vez, retiraria uma funcionalidade por vez do monólito e criaria um serviço novo para ela. As novas requisições passariam gradualmente para o microsserviço, enquanto o restante continuaria sendo atendido pelo monólito. Assim, a migração pode ser testada em etapas e revertida com menos risco.

## 14. Desafio de Implementação

O seu primeiro passo será extrair o contexto de Pagamento. Aplique os princípios de DDD e o padrão de migração escolhido para implementar a estrutura inicial desse novo microserviço em Java. Em seu documento, insira os blocos de código demonstrando: A classe do seu novo Aggregate Root de Pagamento; Como esse novo microsserviço se comunicará com o monolito legado (ex: uma interface ou serviço de integração), garantindo o baixo acoplamento.

**Resposta:** O Pagamento será o Aggregate Root e concentrará as regras principais desse contexto. Durante a migração pelo Strangler Fig, o novo microsserviço ainda precisará conversar com o monólito. Para manter baixo acoplamento, essa comunicação será feita por uma interface (Gateway), permitindo trocar a implementação depois sem alterar o domínio de Pagamento.

### Aggregate Root de Pagamento

```java
package ecommerce.pagamento.domain;

import java.math.BigDecimal;

public class Pagamento {

    private final Long id;
    private final Long pedidoId;
    private final BigDecimal valor;
    private StatusPagamento status;

    public Pagamento(Long id, Long pedidoId, BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        this.id = id;
        this.pedidoId = pedidoId;
        this.valor = valor;
        this.status = StatusPagamento.PENDENTE;
    }

    public void aprovar() {
        if (status != StatusPagamento.PENDENTE) {
            throw new IllegalStateException("Pagamento não está pendente");
        }

        status = StatusPagamento.APROVADO;
    }
}

enum StatusPagamento {
    PENDENTE,
    APROVADO,
    RECUSADO
}
```

### Interface de integração com o monólito

```java
package ecommerce.pagamento.integration;

// Contrato usado pelo microsserviço.
// O domínio não conhece classes do monólito.
public interface MonolitoGateway {

    boolean pedidoExiste(Long pedidoId);

    void informarPagamentoAprovado(Long pedidoId);
}
```

### Serviço de Pagamento

```java
package ecommerce.pagamento.application;

import ecommerce.pagamento.domain.Pagamento;
import ecommerce.pagamento.integration.MonolitoGateway;

public class PagamentoService {

    private final MonolitoGateway monolitoGateway;

    public PagamentoService(MonolitoGateway monolitoGateway) {
        this.monolitoGateway = monolitoGateway;
    }

    public void processar(Pagamento pagamento, Long pedidoId) {
        if (!monolitoGateway.pedidoExiste(pedidoId)) {
            throw new IllegalArgumentException("Pedido inexistente");
        }

        pagamento.aprovar();
        monolitoGateway.informarPagamentoAprovado(pedidoId);
    }
}
```
