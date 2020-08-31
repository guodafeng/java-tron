package org.tron.common.runtime.vm;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tron.common.parameter.CommonParameter;
import org.tron.common.runtime.InternalTransaction;
import org.tron.core.config.args.Args;
import org.tron.core.vm.OpCode;
import org.tron.core.vm.VM;
import org.tron.core.vm.program.Program;
import org.tron.core.vm.program.invoke.ProgramInvokeMockImpl;
import org.tron.protos.Protocol.Transaction;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
public class OriginalBenchMarkTest {

    private ProgramInvokeMockImpl invoke;
    private Program program;

    @BeforeClass
    public static void init() {
        CommonParameter.getInstance().setDebug(true);
    }

    @AfterClass
    public static void destroy() {
        Args.clearParam();
    }

    @Test
    public void test() throws Exception {

        LinkedHashMap<Byte, byte[]> opMap = new LinkedHashMap<>();

        //add
        opMap.put((byte)0x01, new byte[]{0x60, 0x01, 0x5b, 0x60, 0x01, 0x01, 0x60, 0x02, 0x56});
        //mul
//        opMap.put((byte)0x02, new byte[]{0x60, 0x01, 0x5b, 0x60, 0x01, 0x02, 0x60, 0x02, 0x56});
//        //sub
//        opMap.put((byte)0x03, new byte[]{0x60, 0x01, 0x5b, 0x60, 0x01, 0x03, 0x60, 0x02, 0x56});
//        //div
//        opMap.put((byte)0x04, new byte[]{0x60, 0x01, 0x5b, 0x60, 0x01, 0x04, 0x60, 0x02, 0x56});
//        //sdiv
//        opMap.put((byte)0x05, new byte[]{0x60, 0x01, 0x5b, 0x60, 0x01, 0x05, 0x60, 0x02, 0x56});
//        //mod
//        opMap.put((byte)0x06, new byte[]{0x60, 0x01, 0x5b, 0x60, 0x01, 0x06, 0x60, 0x02, 0x56});
//        //smod
//        opMap.put((byte)0x07, new byte[]{0x60, 0x01, 0x5b, 0x60, 0x01, 0x07, 0x60, 0x02, 0x56});
//        //addmod
//        opMap.put((byte)0x08, new byte[]{0x60, 0x01, 0x5b, 0x60, 0x01, 0x60, 0x01, 0x08, 0x60, 0x02, 0x56});
//        //mulmod
//        opMap.put((byte)0x09, new byte[]{0x60, 0x01, 0x5b, 0x60, 0x01, 0x60, 0x01, 0x09, 0x60, 0x02, 0x56});
//        //exp
//        opMap.put((byte)0x0a, new byte[]{0x60, 0x03, 0x5b, 0x60, 0x0b, 0x0a, 0x60, 0x02, 0x56});
//        //SIGNEXTEND
//        opMap.put((byte)0x0b, new byte[]{0x60, 0x01, 0x5b, 0x60, 0x01, 0x0b, 0x60, 0x02, 0x56});
//        //and
//        opMap.put((byte)0x16, new byte[]{0x5b, 0x60, 0x01, 0x60, 0x01, 0x16, 0x50, 0x60, 0x00, 0x56});
//        //or
//        opMap.put((byte)0x17, new byte[]{0x5b, 0x60, 0x01, 0x60, 0x01, 0x17, 0x50, 0x60, 0x00, 0x56});
//        //xor
//        opMap.put((byte)0x18, new byte[]{0x5b, 0x60, 0x01, 0x60, 0x01, 0x18, 0x50, 0x60, 0x00, 0x56});
//        //not
//        opMap.put((byte)0x19, new byte[]{0x60, 0x01, 0x5b, 0x19, 0x60, 0x02, 0x56});

        long billion = 1000000000L;
        long billion5 = 5000000000L;
        long billion10 = 10000000000L;
        long billion20 = 20000000000L;
        long billion40 = 40000000000L;

        //open file
        //String tokenPath = new File(Resouce.get, "token.txt").getAbsolutePath();

        for(int i = 1; i <= 10; i++) {
            //System.out.println("第"+i+"次");
            for (Map.Entry<Byte, byte[]> entry : opMap.entrySet()) {
                VM vm = new VM();
                vm.targetOp = entry.getKey();
                invoke = new ProgramInvokeMockImpl();
                Transaction trx = Transaction.getDefaultInstance();
                InternalTransaction interTrx = new InternalTransaction(trx, InternalTransaction.TrxType.TRX_UNKNOWN_TYPE);
                program = new Program(entry.getValue(), invoke, interTrx);

                while (vm.timeAll < billion) {
                    vm.step(program);
                }

                double time = ((double) vm.timeAll) / vm.count;
                //System.out.println(String.format("\"%s(0x%02x)\"\t\t\t\t%d\t\t\t\t%f",
                //       OpCode.code(entry.getKey()),entry.getKey(), vm.count,time));
                System.out.println(String.format("%d,%s(0x%02x),%d,%d,%f",
                        i,OpCode.code(entry.getKey()),entry.getKey(), vm.timeAll,vm.count,
                        time));
            }
        }

        //close file
    }
}
