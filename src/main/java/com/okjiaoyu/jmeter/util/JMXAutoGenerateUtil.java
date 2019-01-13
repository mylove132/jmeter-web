package com.okjiaoyu.jmeter.util;

import java.io.*;

public class JMXAutoGenerateUtil {

    /**
     * 生成jmeter的版本
     * @param jmeterVersion
     * @return
     */
    public String generateFileHead(String jmeterVersion){
        if (jmeterVersion == null || jmeterVersion.equals("")){
            jmeterVersion = "3.0";
        }
        String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jmeterTestPlan version=\"1.2\" properties=\""+jmeterVersion+"\" jmeter=\""+jmeterVersion+" r1840935\">";
        return head;
    }

    /**
     * 生成jmx的压测头
     * @return
     */
    public String generateTopashTree(){
        String elementHead = " <hashTree>\n" +
                "    <TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"Test Plan\" enabled=\"true\">\n" +
                "      <stringProp name=\"TestPlan.comments\"></stringProp>\n" +
                "      <boolProp name=\"TestPlan.functional_mode\">false</boolProp>\n" +
                "      <boolProp name=\"TestPlan.tearDown_on_shutdown\">true</boolProp>\n" +
                "      <boolProp name=\"TestPlan.serialize_threadgroups\">false</boolProp>\n" +
                "      <elementProp name=\"TestPlan.user_defined_variables\" elementType=\"Arguments\" guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">\n" +
                "        <collectionProp name=\"Arguments.arguments\"/>\n" +
                "      </elementProp>\n" +
                "      <stringProp name=\"TestPlan.user_define_classpath\"></stringProp>\n" +
                "    </TestPlan>\n" +
                "    <hashTree>";

        return elementHead;
    }

    /**
     * 生成压测需求
     * @param preNumber
     * @param preTime
     * @return
     */
    public String generateThreadGroup(int preNumber,int preTime){
        if (preNumber == 0){
            preNumber = 200;
        }
        if (preTime == 0){
            preTime = 120;
        }
       String groupValue = "<ThreadGroup guiclass=\"ThreadGroupGui\" testclass=\"ThreadGroup\" testname=\"Thread Group\" enabled=\"true\">\n" +
               "        <stringProp name=\"ThreadGroup.on_sample_error\">continue</stringProp>\n" +
               "        <elementProp name=\"ThreadGroup.main_controller\" elementType=\"LoopController\" guiclass=\"LoopControlPanel\" testclass=\"LoopController\" testname=\"Loop Controller\" enabled=\"true\">\n" +
               "          <boolProp name=\"LoopController.continue_forever\">false</boolProp>\n" +
               "          <intProp name=\"LoopController.loops\">-1</intProp>\n" +
               "        </elementProp>\n" +
               "        <stringProp name=\"ThreadGroup.num_threads\">"+preNumber+"</stringProp>\n" +
               "        <stringProp name=\"ThreadGroup.ramp_time\">1</stringProp>\n" +
               "        <boolProp name=\"ThreadGroup.scheduler\">true</boolProp>\n" +
               "        <stringProp name=\"ThreadGroup.duration\">"+preTime+"</stringProp>\n" +
               "        <stringProp name=\"ThreadGroup.delay\"></stringProp>\n" +
               "      </ThreadGroup>";
        return groupValue;
    }

    /**
     * 生成压测数据
     * {&quot;java&quot;:{&quot;java.lang.String&quot;:&quot;jdk1.8,maven3.2&quot;},&quot;price&quot;:{&quot;java.lang.Double&quot;:&quot;10001.01,10080.099&quot;},&quot;version&quot;:{&quot;java.lang.Long&quot;:&quot;1000001,100002&quot;}}
     * @return
     */
    public String generatePreData(String preName, String zkAddress, String dubboInterfaceName,
                                  String methodName, String reuqestBeanClass, String params){
        if (preName == null || "".equals(preName)){
            preName = "dubbo sample case";
        }
        if (zkAddress == null || "".equals(zkAddress)){
            zkAddress = "172.18.4.48:2181";
        }
        String dataValue = " <hashTree>\n" +
                "        <com.jmeter.plugin.dubbo.DubboPlugin guiclass=\"com.jmeter.plugin.gui.DubboSamplePluginGui\" testclass=\"com.jmeter.plugin.dubbo.DubboPlugin\" testname=\""+preName+"\" enabled=\"true\">\n" +
                "          <stringProp name=\"ADDRESS\">"+zkAddress+"</stringProp>\n" +
                "          <stringProp name=\"REGISTRY_PROTOCOL\">zookeeper</stringProp>\n" +
                "          <stringProp name=\"DUBBO_REGISTRY_SERVICE\">"+dubboInterfaceName+"</stringProp>\n" +
                "          <stringProp name=\"DUBBO_REGISTRY_METHOD\">"+methodName+"</stringProp>\n" +
                "          <stringProp name=\"REQUEST_BEAN\">"+reuqestBeanClass+"</stringProp>\n" +
                "          <stringProp name=\"DUBBO_PARAMS\">"+params+"</stringProp>\n" +
                "        </com.jmeter.plugin.dubbo.DubboPlugin>";

        return dataValue;
    }
    public String generateWatchResultTree(){
        return "<hashTree>\n" +
                "          <ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"View Results Tree\" enabled=\"true\">\n" +
                "            <boolProp name=\"ResultCollector.error_logging\">false</boolProp>\n" +
                "            <objProp>\n" +
                "              <name>saveConfig</name>\n" +
                "              <value class=\"SampleSaveConfiguration\">\n" +
                "                <time>true</time>\n" +
                "                <latency>true</latency>\n" +
                "                <timestamp>true</timestamp>\n" +
                "                <success>true</success>\n" +
                "                <label>true</label>\n" +
                "                <code>true</code>\n" +
                "                <message>true</message>\n" +
                "                <threadName>true</threadName>\n" +
                "                <dataType>true</dataType>\n" +
                "                <encoding>false</encoding>\n" +
                "                <assertions>true</assertions>\n" +
                "                <subresults>true</subresults>\n" +
                "                <responseData>false</responseData>\n" +
                "                <samplerData>false</samplerData>\n" +
                "                <xml>false</xml>\n" +
                "                <fieldNames>true</fieldNames>\n" +
                "                <responseHeaders>false</responseHeaders>\n" +
                "                <requestHeaders>false</requestHeaders>\n" +
                "                <responseDataOnError>false</responseDataOnError>\n" +
                "                <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>\n" +
                "                <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                "                <bytes>true</bytes>\n" +
                "                <sentBytes>true</sentBytes>\n" +
                "                <url>true</url>\n" +
                "                <threadCounts>true</threadCounts>\n" +
                "                <idleTime>true</idleTime>\n" +
                "                <connectTime>true</connectTime>\n" +
                "              </value>\n" +
                "            </objProp>\n" +
                "            <stringProp name=\"filename\"></stringProp>\n" +
                "          </ResultCollector>\n" +
                "          <hashTree/>";
    }
    public String generateSumResult(){
        return "<ResultCollector guiclass=\"SummaryReport\" testclass=\"ResultCollector\" testname=\"Summary Report\" enabled=\"true\">\n" +
                "            <boolProp name=\"ResultCollector.error_logging\">false</boolProp>\n" +
                "            <objProp>\n" +
                "              <name>saveConfig</name>\n" +
                "              <value class=\"SampleSaveConfiguration\">\n" +
                "                <time>true</time>\n" +
                "                <latency>true</latency>\n" +
                "                <timestamp>true</timestamp>\n" +
                "                <success>true</success>\n" +
                "                <label>true</label>\n" +
                "                <code>true</code>\n" +
                "                <message>true</message>\n" +
                "                <threadName>true</threadName>\n" +
                "                <dataType>true</dataType>\n" +
                "                <encoding>false</encoding>\n" +
                "                <assertions>true</assertions>\n" +
                "                <subresults>true</subresults>\n" +
                "                <responseData>false</responseData>\n" +
                "                <samplerData>false</samplerData>\n" +
                "                <xml>false</xml>\n" +
                "                <fieldNames>true</fieldNames>\n" +
                "                <responseHeaders>false</responseHeaders>\n" +
                "                <requestHeaders>false</requestHeaders>\n" +
                "                <responseDataOnError>false</responseDataOnError>\n" +
                "                <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>\n" +
                "                <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                "                <bytes>true</bytes>\n" +
                "                <sentBytes>true</sentBytes>\n" +
                "                <url>true</url>\n" +
                "                <threadCounts>true</threadCounts>\n" +
                "                <idleTime>true</idleTime>\n" +
                "                <connectTime>true</connectTime>\n" +
                "              </value>\n" +
                "            </objProp>\n" +
                "            <stringProp name=\"filename\"></stringProp>\n" +
                "          </ResultCollector>\n" +
                "          <hashTree/>\n" +
                "        </hashTree>\n" +
                "      </hashTree>\n" +
                "    </hashTree>\n" +
                "  </hashTree>\n" +
                "</jmeterTestPlan>";
    }
    public static void main(String[] args) throws IOException {
        JMXAutoGenerateUtil jmxAutoGenerate = new JMXAutoGenerateUtil();
        String generateValue = jmxAutoGenerate.generateFileHead("5.0")+
                jmxAutoGenerate.generateTopashTree()+
                jmxAutoGenerate.generateThreadGroup(300,0)+
                jmxAutoGenerate.generatePreData("美团外卖","","com.noriental.lessonsvr.rservice.ResPackageService","findPublishStudentByClass",
                        "com.noriental.lessonsvr.entity.request.FindPublishStudentRequest",
                        "{java:{java.lang.String:jdk1.8,maven3.2},price:{java.lang.Double:10001.01,10080.099},version:{java.lang.Long:1000001,100002}}")+
                jmxAutoGenerate.generateWatchResultTree()+
                jmxAutoGenerate.generateSumResult();
        OutputStream os = new FileOutputStream(new File("D:\\test.jmx"));
        os.write(generateValue.getBytes());
        os.close();
        //System.out.println(jmxAutoGenerate.generateWatchResultTree());
    }
}
