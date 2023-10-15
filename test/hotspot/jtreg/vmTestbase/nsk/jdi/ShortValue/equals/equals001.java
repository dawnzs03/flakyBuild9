/*
 * Copyright (c) 2000, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package nsk.jdi.ShortValue.equals;

import nsk.share.*;
import nsk.share.jpda.*;
import nsk.share.jdi.*;

import com.sun.jdi.*;
import java.util.*;
import java.io.*;

/**
 * The test for the implementation of an object of the type     <BR>
 * ShortValue.                                                  <BR>
 *                                                              <BR>
 * The test checks up that results of the method                <BR>
 * <code>com.sun.jdi.ShortValue.equals()</code>                 <BR>
 * complies with its spec.                                      <BR>
 * <BR>
 * The cases for testing are as follows :               <BR>
 *                                                      <BR>
 * when a gebuggee executes the following :             <BR>
 *      public static short plus1_1 = +1;               <BR>
 *      public static short plus1_2 = +1;               <BR>
 *      public static short minus1  = -1;               <BR>
 *      public static int intplus1  = +1;               <BR>
 *                                                      <BR>
 * which a debugger mirros as :                         <BR>
 *                                                      <BR>
 *      ShortValue   svplus1_1;                         <BR>
 *      ShortValue   svplus1_2;                         <BR>
 *      ShortValue   svminus1;                          <BR>
 *      IntegerValue ivplus1;                           <BR>
 *                                                      <BR>
 * the following is true:                               <BR>
 *                                                      <BR>
 *      svplus1_1 == svplus1_2                          <BR>
 *      svplus1_1 != svminus1                           <BR>
 *      svplus1_1 != ivplus1                            <BR>
 * <BR>
 */

public class equals001 {

    //----------------------------------------------------- templete section
    static final int PASSED = 0;
    static final int FAILED = 2;
    static final int PASS_BASE = 95;

    //----------------------------------------------------- templete parameters
    static final String
    sHeader1 = "\n==> nsk/jdi/ShortValue/equals/equals001",
    sHeader2 = "--> equals001: ",
    sHeader3 = "##> equals001: ";

    //----------------------------------------------------- main method

    public static void main (String argv[]) {
        int result = run(argv, System.out);
        System.exit(result + PASS_BASE);
    }

    public static int run (String argv[], PrintStream out) {
        return new equals001().runThis(argv, out);
    }

     //--------------------------------------------------   log procedures

    private static boolean verbMode = false;

    private static Log  logHandler;

    private static void log1(String message) {
        logHandler.display(sHeader1 + message);
    }
    private static void log2(String message) {
        logHandler.display(sHeader2 + message);
    }
    private static void log3(String message) {
        logHandler.complain(sHeader3 + message);
    }

    //  ************************************************    test parameters

    private String debuggeeName =
        "nsk.jdi.ShortValue.equals.equals001a";

    //====================================================== test program

    static ArgumentHandler      argsHandler;
    static int                  testExitCode = PASSED;

    //------------------------------------------------------ common section

    private int runThis (String argv[], PrintStream out) {

        Debugee debuggee;

        argsHandler     = new ArgumentHandler(argv);
        logHandler      = new Log(out, argsHandler);
        Binder binder   = new Binder(argsHandler, logHandler);

        if (argsHandler.verbose()) {
            debuggee = binder.bindToDebugee(debuggeeName + " -vbs");  // *** tp
        } else {
            debuggee = binder.bindToDebugee(debuggeeName);            // *** tp
        }

        IOPipe pipe     = new IOPipe(debuggee);

        debuggee.redirectStderr(out);
        log2("equals001a debuggee launched");
        debuggee.resume();

        String line = pipe.readln();
        if ((line == null) || !line.equals("ready")) {
            log3("signal received is not 'ready' but: " + line);
            return FAILED;
        } else {
            log2("'ready' recieved");
        }

        VirtualMachine vm = debuggee.VM();

    //------------------------------------------------------  testing section
        log1("      TESTING BEGINS");

        for (int i = 0; ; i++) {
            pipe.println("newcheck");
            line = pipe.readln();

            if (line.equals("checkend")) {
                log2("     : returned string is 'checkend'");
                break ;
            } else if (!line.equals("checkready")) {
                log3("ERROR: returned string is not 'checkready'");
                testExitCode = FAILED;
                break ;
            }

            log1("new check: #" + i);

            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ variable part

            List listOfDebuggeeExecClasses = vm.classesByName(debuggeeName);
            if (listOfDebuggeeExecClasses.size() != 1) {
                testExitCode = FAILED;
                log3("ERROR: listOfDebuggeeExecClasses.size() != 1");
                break ;
            }
            ReferenceType execClass =
                        (ReferenceType) listOfDebuggeeExecClasses.get(0);

            Field fsplus1_1 = execClass.fieldByName("plus1_1");
            Field fsplus1_2 = execClass.fieldByName("plus1_2");
            Field fsminus1  = execClass.fieldByName("minus1");
            Field fiplus1   = execClass.fieldByName("intplus1");

            ShortValue svplus1_1 = (ShortValue)   execClass.getValue(fsplus1_1);
            ShortValue svplus1_2 = (ShortValue)   execClass.getValue(fsplus1_2);
            ShortValue svminus1  = (ShortValue)   execClass.getValue(fsminus1);
            IntegerValue ivplus1 = (IntegerValue) execClass.getValue(fiplus1);

            int i2;

            for (i2 = 0; ; i2++) {

                int expresult = 0;

                log2("new check: #" + i2);

                switch (i2) {

                case 0: if (!svplus1_1.equals(svplus1_2))
                            expresult = 1;
                        break;

                case 1: if (svplus1_1.equals(svminus1))
                            expresult = 1;
                        break;

                case 2: if (svplus1_1.equals(ivplus1))
                            expresult = 1;
                        break;


                default: expresult = 2;
                         break ;
                }

                if (expresult == 2) {
                    log2("      test cases finished");
                    break ;
                } else if (expresult == 1) {
                    log3("ERROR: expresult != true;  check # = " + i);
                    testExitCode = FAILED;
                }
            }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        }
        log1("      TESTING ENDS");

    //--------------------------------------------------   test summary section
    //-------------------------------------------------    standard end section

        pipe.println("quit");
        log2("waiting for the debuggee to finish ...");
        debuggee.waitFor();

        int status = debuggee.getStatus();
        if (status != PASSED + PASS_BASE) {
            log3("debuggee returned UNEXPECTED exit status: " +
                   status + " != PASS_BASE");
            testExitCode = FAILED;
        } else {
            log2("debuggee returned expected exit status: " +
                   status + " == PASS_BASE");
        }

        if (testExitCode != PASSED) {
            logHandler.complain("TEST FAILED");
        }
        return testExitCode;
    }
}
