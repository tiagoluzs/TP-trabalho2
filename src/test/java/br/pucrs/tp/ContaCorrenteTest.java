package br.pucrs.tp;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class ContaCorrenteTest
{
    private ContaCorrente conta;
    @BeforeEach
    public void initialize()
    {
        conta = new ContaCorrente("0000", "test");
    }

    @Test
    public void checkAccountConstruct()
    {
        Assertions.assertEquals("test", conta.getNomeCorrentista());
        Assertions.assertEquals("0000", conta.getNumeroConta());
    }

    //------------------Testing category change--------------------
    //---- testing change in the category based on deposit ----
    @ParameterizedTest
    @MethodSource("arguments_singleDeposit")
    public void testCategory_singleDeposit(double depositValue, Categoria expectedCategory)
    {
        conta.deposito(depositValue);
        Assertions.assertEquals(conta.getCategoria(), expectedCategory);
    }

    private static Stream<Arguments> arguments_singleDeposit() {
	    return Stream.of(
	      Arguments.of(49999, Categoria.Silver), // Entering the deposit of $49999 should still maintain the silver category
	      Arguments.of(50000, Categoria.Gold), // Entering the deposit of $50000 should change the category to gold
	      Arguments.of(200000, Categoria.Gold) // Entering the deposit of $200000 should not change from silver to platinum, instead, should still change just to gold
	    );
	}

    @ParameterizedTest
    @MethodSource("arguments_twoDeposit")
    public void testCategory_twoDeposit(double firstDepositValue, double secondDepositValue, Categoria expectedCategory)
    {
        conta.deposito(firstDepositValue);
        conta.deposito(secondDepositValue);
        Assertions.assertEquals(conta.getCategoria(), expectedCategory);
    }

    private static Stream<Arguments> arguments_twoDeposit() {
	    return Stream.of(
	      Arguments.of(49999.0, 148513.87, Categoria.Gold), // Entering the deposit of $148513,87 (plus 1%, which is 1485,13) should still maintain the gold category
	      Arguments.of(50000.0, 148514.86, Categoria.Platinum), // Entering the deposit of $148514,86 (plus 1%, which is $1485,14) should change the category to platinum
	      Arguments.of(200000.0, 1.0, Categoria.Platinum) // Entering the deposit of $200000 should not change from silver to platinum, instead, should still change just to gold, but after a deposit of $1 it should change to platinum
	    );
	}

    //---- testing change in the category based on withdraw ----
    @ParameterizedTest
    @MethodSource("arguments_oneDepositOneWithdraw")
    public void testCategory_oneDepositOneWithdraw(double depositValue, double withdrawValue, Categoria expectedCategory)
    {
        conta.deposito(depositValue);
        conta.retirada(withdrawValue);
        Assertions.assertEquals(conta.getCategoria(), expectedCategory);
    }

    private static Stream<Arguments> arguments_oneDepositOneWithdraw()
    {
	    return Stream.of(
	      Arguments.of(50000.0, 25000.0, Categoria.Gold), // Lowering the balance to $25000 should not change the category to silver
	      Arguments.of(50000.0, 25001.0, Categoria.Silver) // Lowering the balance to $24999 should change the category to silver
	    );
	}

    @ParameterizedTest
    @MethodSource("arguments_twoDepositOneWithdraw")
    public void testCategory_twoDepositOneWithdraw(double firstDepositValue, double secondDepositValue, double withdrawValue, Categoria expectedCategory)
    {
        conta.deposito(firstDepositValue);
        conta.deposito(secondDepositValue);
        conta.retirada(withdrawValue);
        Assertions.assertEquals(conta.getCategoria(), expectedCategory);
    }

    private static Stream<Arguments> arguments_twoDepositOneWithdraw()
    {
	    return Stream.of(
	      Arguments.of(50000.0, 150000.0, 100000.0, Categoria.Platinum), // Lowering the balance to 100000 should not change the category to gold, instead, it should still be platinum
	      Arguments.of(50000.0, 150000.0, 120000.0, Categoria.Gold), // Lowering the balance below $100000 should change the category to gold
	      Arguments.of(50000.0, 150000.0, 180000.0, Categoria.Gold) // Lowering the balance to $20000 should not change from platinum to silver, instead, should still change just to gold
	    );
	}

    // testing to see if the category will not change to platinum after a big withdraw, instead to gold, but change to silver after a proper withdraw
    //lowering the balance to $24999 should not change from platinum to silver, but after another withdraw of $1 it should change to silver
    @Test
    public void twoWithdrawsToChangeToSilver()
    {        
        conta.deposito(50000);
        conta.deposito(150000);
        conta.retirada(180000);
        conta.retirada(1);        
        Categoria result = conta.getCategoria();
        Assertions.assertEquals(Categoria.Silver, result);
    }

    //--------------- Testing deposits values---------------
    //testing to see if the deposit is working as silver category
    //depositing $10000 should change the balance to $10000
    @Test
    public void balanceDepositAsSilver()
    {        
        conta.deposito(10000);
        double result = conta.getSaldo();
        Assertions.assertEquals(10000, result);
    }
    //testing to see if the deposit is working as gold category
    //depositing $10000, based of $50000, should change the balance to $60100
    @Test
    public void balanceDepositAsGold()
    {        
        conta.deposito(50000);
        conta.deposito(10000);
        double result = conta.getSaldo();
        Assertions.assertEquals(60100, result);
    }
    //testing to see if the deposit is working as platinum category
    //depositing $10000, based of $200000, should change the balance to $210250
    @Test
    public void balanceDepositAsPlatinum()
    {
        conta.deposito(50000);
        conta.deposito(148514.86);
        conta.deposito(10000);
        double result = Math.abs(210250 - conta.getSaldo());
        Assertions.assertTrue(result < 0.01);
    }

    //--------------- Testing invalid values---------------
    @ParameterizedTest()
    @CsvSource({
        "0",
        "-1"
    })
    public void invalidDepositValues(double value)
    {
        boolean result = conta.deposito(value);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest()
    @CsvSource({
        "0",
        "-1"
    })
    public void invalidWithdrawValues(double value)
    {
        boolean result = conta.retirada(value);
        Assertions.assertFalse(result);
    }

    @Test
    public void moreThanICanTake()
    {
        conta.deposito(100);
        boolean withdraw = conta.retirada(101);
        Assertions.assertFalse(withdraw);

        double result = conta.getSaldo();
        Assertions.assertEquals(100, result);
    }
}
