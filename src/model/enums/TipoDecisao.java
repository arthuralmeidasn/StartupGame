package model.enums;

// Tipos de decisão disponíveis
public enum TipoDecisao {
    MARKETING, // +reputação; bônus % receita na próxima rodada; -caixa
    EQUIPE, // +moral; -caixa
    PRODUTO, // bônus % receita na próxima rodada; -caixa
    INVESTIDORES, // chance de +caixa e +reputação; senão -reputação
    CORTAR_CUSTOS // +caixa; -moral
}
