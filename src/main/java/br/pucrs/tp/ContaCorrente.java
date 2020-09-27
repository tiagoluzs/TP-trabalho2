package br.pucrs.tp;

/**
* Trabalho 4 - Testes Unitários
* @author Rauf L. F. Rodrigues
* @author Tiago Luz
* @author Lennon da Silva Rocha
*/

public class ContaCorrente
{
    private String nroConta;
    private String nome;
    private double saldo;
    private Categoria categoria;

    public ContaCorrente(String nro, String nome)
    {
        this.nroConta = nro;
        this.nome = nome;
        this.saldo = 0.00;
        this.categoria = Categoria.Silver;
    }

    public String getNumeroConta()
    {
        return nroConta;
    }
    public String getNomeCorrentista()
    {
        return nome;
    }
    public double getSaldo()
    {
        return saldo;
    }
    public Categoria getCategoria()
    {
        return categoria;
    }

    public boolean deposito(double valor)
    {
        if (valor <= 0) {
            return false; //atenção para testar essa condição
        }

        if (categoria == Categoria.Silver) {
            saldo += valor;
            if (saldo >= 50000) {
                categoria = Categoria.Gold;
                return true;
            }
        }

        else if (categoria == Categoria.Gold) {
            saldo += valor + (valor*0.01);
            if (saldo >= 200000) {
                categoria = Categoria.Platinum;
            }
        }

        else if (categoria == Categoria.Platinum) {
            saldo += valor + (valor*0.025);
        }
        return true;
    }

    public boolean retirada(double valor)
    {
        if (valor <= 0) {
            return false; //atenção para testar essa condição
        }
        if ((saldo - valor) < 0) {            
            return false;
        }
        saldo -= valor;
        if (categoria == Categoria.Platinum && saldo < 100000) {
            categoria = Categoria.Gold;
        }
        else if (categoria == Categoria.Gold && saldo < 25000) {
            categoria = Categoria.Silver;
        }
        
        return true;
    }

    public static void main(String args[])
    {
        ContaCorrente contaCorrente = new ContaCorrente("21300-4", "Leno");
        System.out.println("Conta: " + contaCorrente.getNumeroConta() +  " - Correntista: " + contaCorrente.getNomeCorrentista());
    }
}
