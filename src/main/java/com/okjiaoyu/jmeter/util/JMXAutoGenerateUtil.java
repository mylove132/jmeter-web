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
     * @return
     */
    public String generatePreData(String timeOut, String preName, String zkAddress, String dubboInterfaceName,
                                  String methodName, String reuqestBeanClass, String params){
        if (preName == null || "".equals(preName)){
            preName = "dubbo sample";
        }
        if (zkAddress == null || "".equals(zkAddress)){
            zkAddress = "172.18.4.48:2181";
        }
        String dataValue = "<hashTree>\n" +
                "        <com.jmeter.plugin.dubbo.DubboSample guiclass=\"com.jmeter.plugin.gui.DubboSampleGui\" testclass=\"com.jmeter.plugin.dubbo.DubboSample\" testname=\""+preName+"\" enabled=\"true\">\n" +
                "          <stringProp name=\"FIELD_DUBBO_REGISTRY_PROTOCOL\">zookeeper</stringProp>\n" +
                "          <stringProp name=\"FIELD_DUBBO_RPC_PROTOCOL\">dubbo://</stringProp>\n" +
                "          <stringProp name=\"FIELD_DUBBO_ADDRESS\">"+zkAddress+"</stringProp>\n" +
                "          <stringProp name=\"FIELD_DUBBO_TIMEOUT\">"+timeOut+"</stringProp>\n" +
                "          <stringProp name=\"FIELD_DUBBO_INTERFACE\">"+dubboInterfaceName+"</stringProp>\n" +
                "          <stringProp name=\"FIELD_DUBBO_METHOD\">"+methodName+"</stringProp>\n" +
                "          <intProp name=\"FIELD_DUBBO_METHOD_ARGS_SIZE\">1</intProp>\n" +
                "          <stringProp name=\"FIELD_DUBBO_METHOD_ARGS_PARAM_TYPE1\">"+reuqestBeanClass+"</stringProp>\n" +
                "          <stringProp name=\"FIELD_DUBBO_METHOD_ARGS_PARAM_VALUE1\">"+params+"</stringProp>\n" +
                "        </com.jmeter.plugin.dubbo.DubboSample>";

        return dataValue;
    }

    public String assertGui(String assertText){
        return "<hashTree>\n" +
                "          <ResponseAssertion guiclass=\"AssertionGui\" testclass=\"ResponseAssertion\" testname=\"响应断言\" enabled=\"true\">\n" +
                "            <collectionProp name=\"Asserion.test_strings\">\n" +
                "              <stringProp name=\"67791721\">"+assertText+"</stringProp>\n" +
                "            </collectionProp>\n" +
                "            <stringProp name=\"Assertion.test_field\">Assertion.response_data</stringProp>\n" +
                "            <boolProp name=\"Assertion.assume_success\">false</boolProp>\n" +
                "            <intProp name=\"Assertion.test_type\">16</intProp>\n" +
                "          </ResponseAssertion>\n" +
                "          <hashTree/>";
    }
    public String generateWatchResultTree(){
        return "<ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"察看结果树\" enabled=\"true\">\n" +
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
                "                <threadCounts>true</threadCounts>\n" +
                "                <idleTime>true</idleTime>\n" +
                "                <connectTime>true</connectTime>\n" +
                "              </value>\n" +
                "            </objProp>\n" +
                "            <stringProp name=\"filename\"></stringProp>\n" +
                "          </ResultCollector>";
    }
    public String generateSumResult(){
        return "<hashTree/>\n" +
                "          <ResultCollector guiclass=\"StatVisualizer\" testclass=\"ResultCollector\" testname=\"聚合报告\" enabled=\"true\">\n" +
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
        String generateValue = jmxAutoGenerate.generateFileHead("3.1")+
                jmxAutoGenerate.generateTopashTree()+
                jmxAutoGenerate.generateThreadGroup(300,0)+
                jmxAutoGenerate.generatePreData("5000","批量查询","","com.noriental.adminsvr.service.teaching.ChapterService","findByIds",
                        "com.noriental.adminsvr.request.RequestEntity",
                        "{\"entity\":[200,201,202]}")+
                jmxAutoGenerate.assertGui("\"code\":0")+jmxAutoGenerate.generateWatchResultTree()+
                jmxAutoGenerate.generateSumResult();
        OutputStream os = new FileOutputStream(new File("/Users/liuzhanhui/Downloads/test.jmx"));
        os.write(generateValue.getBytes());
        os.close();
        //System.out.println(jmxAutoGenerate.generateWatchResultTree());
    }
}
