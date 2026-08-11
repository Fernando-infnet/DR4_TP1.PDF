# DR4_TP1.PDF

1. Explique de forma sucinta o que são microsserviços.
Resposta: Microsserviços são pequenos serviços de software separados, cada um responsável por uma
parte específica do negócio. Eles podem ser desenvolvidos, implantados e atualizados de forma
independente e se comunicam normalmente por APIs ou mensagens.
2. Cite uma vantagem da arquitetura de microsserviços.
Resposta: Uma vantagem é permitir a evolução independente de partes do sistema. Por exemplo, o
serviço de Pagamento pode ser atualizado sem precisar publicar novamente todo o sistema.
3. Cite uma desvantagem da arquitetura de microsserviços.
Resposta: Uma desvantagem é o aumento da complexidade operacional. Como existem vários serviços
separados, é necessário controlar comunicação, monitoramento, falhas de rede e implantação de cada
serviço.
4. Cite uma característica dos microsserviços que os diferenciam de outras arquiteturas de
software.
Resposta: Uma característica importante é a implantação independente: cada microsserviço pode ter
seu próprio ciclo de desenvolvimento e ser colocado em produção sem exigir a implantação dos demais.
5. Explique de forma sucinta o que é um monólito.
Resposta: Um monólito é uma aplicação em que várias funcionalidades do sistema ficam reunidas em
uma única unidade de software. Normalmente, os módulos são construídos e implantados juntos, mesmo
quando possuem responsabilidades diferentes.
6. Explique de forma sucinta o que significa Acoplamento do ponto de vista de Engenharia de
Software.
Resposta: Acoplamento é o nível de dependência entre partes do sistema. Quanto maior o
acoplamento, maior a chance de uma alteração em um módulo exigir mudanças em outros módulos. Em
geral, busca-se baixo acoplamento.
7. Explique de forma sucinta o que significa Coesão do ponto de vista de Engenharia de Software.
Resposta: Coesão indica o quanto os elementos de um módulo estão relacionados à mesma
responsabilidade. Um módulo com alta coesão reúne funções que pertencem ao mesmo objetivo e evita
misturar regras sem relação entre si.
8. Explique de forma sucinta o que é um Agregado do DDD.
Resposta: Um Agregado é um conjunto de objetos do domínio que deve ser tratado como uma única
unidade de consistência. Ele possui uma raiz, chamada Aggregate Root, que controla as operações e
protege as regras do conjunto.
9. Cite uma vantagem de construir microsserviços usando Agregados do DDD.
Resposta: Os Agregados ajudam a definir limites claros para as regras de negócio. Isso facilita separar
responsabilidades e criar microsserviços mais coesos, com menos dependência de dados e regras
pertencentes a outros serviços.
10. Dê um exemplo de mapeamento de um Contexto Delimitado para um microserviço, utilizando a
linguagem Java.
Resposta: Um Contexto Delimitado pode virar um serviço responsável apenas por aquele domínio. No
exemplo abaixo, o contexto de Usuário fica isolado em um microserviço próprio, sem colocar regras de
Estoque ou Pagamento na mesma classe.
package ecommerce.usuario;
// Exemplo simples de um contexto delimitado de Usuário.
public class UsuarioService {
 public Usuario buscarUsuario(Long id) {
 // Regras e dados pertencentes ao contexto de Usuário.
 return new Usuario(id, "Cliente");
 }
}
record Usuario(Long id, String nome) {}
11. Dê um exemplo de mapeamento de um Agregado, com Objeto de Valor, para o contexto de um
microserviço, utilizando a linguagem Java.
Resposta: Neste exemplo, Pagamento é a raiz do Agregado e Dinheiro é um Objeto de Valor. As regras
do pagamento ficam concentradas nesse domínio, evitando que outros módulos alterem diretamente seu
estado.
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
12. Porque o compartilhamento de banco de dados é uma estratégia de integração ruim sob o
ponto de vista de microsserviços?
Resposta: Porque os serviços ficam dependentes do mesmo esquema de banco. Uma alteração em
uma tabela pode quebrar vários serviços, além de permitir que um serviço acesse dados internos de
outro. O ideal é cada microsserviço controlar seus próprios dados e expor apenas APIs ou eventos
necessários.
Parte 2 - Estudo de Caso e Refatoração Prática
Cenário: Você assumiu a liderança técnica de um sistema monolítico de E-commerce legado. Atualmente,
os módulos de Usuário, Estoque e Pagamento dividem o mesmo banco de dados e possuem classes
fortemente acopladas, o que tem gerado travamentos no sistema durante picos de acesso. A diretoria
aprovou a migração gradual para microsserviços.

13. Explique, com suas próprias palavras, qual estratégia de migração (como o padrão Strangler
Fig / Padrão Estrangulador ou Branch by Abstraction) você utilizaria para iniciar a transformação
desse monólito em microsserviços de forma segura, sem precisar desligar o sistema atual.
Resposta: Eu utilizaria o padrão Strangler Fig. Em vez de reescrever todo o sistema de uma vez,
retiraria uma funcionalidade por vez do monólito e criaria um serviço novo para ela. As novas requisições
passariam gradualmente para o microsserviço, enquanto o restante continuaria sendo atendido pelo
monólito. Assim, a migração pode ser testada em etapas e revertida com menos risco.
14. Desafio de Implementação: O seu primeiro passo será extrair o contexto de Pagamento. Aplique
os princípios de DDD e o padrão de migração escolhido para implementar a estrutura inicial desse
novo microserviço em Java. Em seu documento, insira os blocos de código demonstrando: A
classe do seu novo Aggregate Root de Pagamento; Como esse novo microsserviço se comunicará
com o monolito legado (ex: uma interface ou serviço de integração), garantindo o baixo
acoplamento.
Resposta: O Pagamento será o Aggregate Root e concentrará as regras principais desse contexto.
Durante a migração pelo Strangler Fig, o novo microsserviço ainda precisará conversar com o monólito.
Para manter baixo acoplamento, essa comunicação será feita por uma interface (Gateway), permitindo
trocar a implementação depois sem alterar o domínio de Pagamento.
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
 PENDENTE, APROVADO, RECUSADO
}
Interface de integração com o monólito:
package ecommerce.pagamento.integration;
// Contrato usado pelo microsserviço. O domínio não conhece classes do monólito.
public interface MonolitoGateway {
 boolean pedidoExiste(Long pedidoId);
 void informarPagamentoAprovado(Long pedidoId);
}
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
