package br.pucrs.tp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContaCorrenteTest {
    private ContaCorrente conta;
    @BeforeEach
    public void initialize(){
        conta = new ContaCorrente("test", "0000");
    }

    //------------------Testing category change--------------------
    //---- testing change in the category based on deposit ----
    // testing limit values on the threshold (or limit values) between silver and gold, in this case, should return silver after the deposit;
    // entering the deposit of $49999 should still maintain the silver category
    @Test
    public void stillSilver(){
        Categoria result = conta.getCategoria();
        conta.deposito(49999);
        Assertions.assertEquals(Categoria.Silver, result);
    }

    // testing limit values on the threshold (or limit values) between silver and gold, in this case, should return gold after the deposit;
    // entering the deposit of $50000 should change the category to gold
    @Test
    public void silverToGold(){        
        conta.deposito(50000);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Gold, result);
    }

    // testing limit values on the threshold (or limit values) between gold and platinum, in this case, should return gold after the deposit;
    // entering the deposit of $148513,87 (plus 1%, which is 1485,13) should still maintain the gold category
    @Test
    public void stillGold(){        
        conta.deposito(50000);
        conta.deposito(148513.87);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Gold, result);

    }
    
    // testing limit values on the threshold (or limit values) between gold and platinum, in this case, should return platinum after the deposit;
    // entering the deposit of $148514,86 (plus 1%, which is $1485,14) should change the category to platinum
    @Test
    public void goldToPlatinum(){        
        conta.deposito(50000);
        conta.deposito(148514.86);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Platinum, result);
    }

    //---- testing change in the category based on amount deposited ----
    // testing to see if category will not change more than once per deposit
    //entering the deposit of $200000 should not change from silver to platinum, instead, should still change just to gold
    @Test
    public void noChangeFromSilverToPlatinum(){        
        conta.deposito(200000);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Gold, result);
    }

    // testing to see if the category will not change to platinum after a big deposit, instead to gold, but change to platinum after a proper deposit
    //entering the deposit of $200000 should not change from silver to platinum, instead, should still change just to gold, but after a deposit of $1 it should change to platinum
    @Test
    public void twoDepositsToChangeToPlatinum(){        
        conta.deposito(200000);
        conta.deposito(1);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Platinum, result);
    }

    //---- testing change in the category based on withdraw ----
    // testing limit values on the threshold (or limit values) between platinum and gold, in this case, should return platinum after the withdraw;
    //lowering the balance to 100000 should not change the category to gold, instead, it should still be platinum
    @Test
    public void stillPlatinum(){        
        conta.deposito(50000);
        conta.deposito(150000);
        conta.retirada(100000);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Platinum, result);
    }

    // testing limit values on the threshold (or limit values) between platinum and gold, in this case, should return gold after the withdraw;
    //lowering the balance to $99999 should change the category to gold
    @Test
    public void PlatinumToGold(){        
        conta.deposito(50000);
        conta.deposito(150000);
        conta.retirada(120000);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Gold, result);
    }

    // testing limit values on the threshold (or limit values) between gold and silver, in this case, should still return gold after the withdraw;
    //lowering the balance to $25000 should not change the category to silver
    @Test
    public void mantainGold(){        
        conta.deposito(50000);        
        conta.retirada(25000);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Gold, result);
    }

    //testing limit values on the threshold (or limit values) between gold and silver, in this case, should return silver after the withdraw;
    //lowering the balance to $24999 should change the category to silver
    @Test
    public void goldToSilver(){
        conta.deposito(50000);
        conta.retirada(25001);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Silver, result);
    }

    //---- testing change in the category based on amount withdrawn ----
    // testing to see if category will not change more than once per withdraw
    //lowering the balance to $24999 should not change from platinum to silver, instead, should still change just to gold
    @Test
    public void noChangeFromPlatinumToSilver(){        
        conta.deposito(50000);
        conta.deposito(150000);
        conta.retirada(125001);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Gold, result);
    }

    // testing to see if the category will not change to platinum after a big withdraw, instead to gold, but change to silver after a proper withdraw
    //lowering the balance to $24999 should not change from platinum to silver, but after another withdraw of $1 it should change to silver
    @Test
    public void twoWithdrawsToChangeToSilver(){        
        conta.deposito(50000);
        conta.deposito(150000);
        conta.retirada(125001);
        conta.retirada(1);
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Silver, result);
    }
    //--------------- Testing deposits values---------------
    //testing to see if the deposit is working as silver category
    //depositing $10000 should change the balance to $10000
    @Test
    public void balanceDepositAsSilver(){
        double result = conta.getSaldo();
        conta.deposito(10000);
        Assertions.assertEquals(10000, result);
    }
    //testing to see if the deposit is working as gold category
    //depositing $10000, based of $50000, should change the balance to $60100
    @Test
    public void balanceDepositAsGold(){
        double result = conta.getSaldo();
        conta.deposito(50000);
        conta.deposito(10000);
        Assertions.assertEquals(60100, result);
    }
    //testing to see if the deposit is working as platinum category
    //depositing $10000, based of $200000, should change the balance to $200250
    @Test
    public void balanceDepositAsPlatinum(){
        double result = conta.getSaldo();
        conta.deposito(50000);
        conta.deposito(148514.86);
        conta.deposito(10000);
        Assertions.assertEquals(200250, result);
    }

    //--------------- Testing invalid values---------------
    public void negativeDeposit(){
        boolean result = conta.deposito(-1);
        Assertions.assertFalse(result);
    }

    public void zeroDeposit(){
        boolean result = conta.deposito(0);
        Assertions.assertFalse(result);
    }
    
    public void negativeWithdraw(){
        boolean result = conta.retirada(-1);
        Assertions.assertFalse(result);
    }

    public void zeroWithdraw(){
        boolean result = conta.retirada(0);
        Assertions.assertFalse(result);
    }

    public void moreThanICanTake(){
        conta.deposito(100);
        conta.retirada(101);
        double result = conta.getSaldo();
        Assertions.assertEquals(0, result);
    }
}
