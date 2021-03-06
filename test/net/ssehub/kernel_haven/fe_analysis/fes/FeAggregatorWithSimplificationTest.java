/*
 * Copyright 2017-2019 University of Hildesheim, Software Systems Engineering
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ssehub.kernel_haven.fe_analysis.fes;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.fe_analysis.Settings;
import net.ssehub.kernel_haven.fe_analysis.Settings.SimplificationType;
import net.ssehub.kernel_haven.fe_analysis.fes.FeatureEffectFinder.VariableWithFeatureEffect;
import net.ssehub.kernel_haven.logic_utils.LogicUtils;
import net.ssehub.kernel_haven.test_utils.TestAnalysisComponentProvider;
import net.ssehub.kernel_haven.test_utils.TestConfiguration;
import net.ssehub.kernel_haven.util.StaticClassLoader;
import net.ssehub.kernel_haven.util.logic.Negation;
import net.ssehub.kernel_haven.util.logic.True;
import net.ssehub.kernel_haven.util.logic.Variable;

/**
 * Tests the {@link FeAggregator} class, with simplification.
 * 
 * @author Adam
 */
public class FeAggregatorWithSimplificationTest {
    
    /**
     * Makes sure that LogicUtils has been initialized. This is only needed in test cases, because
     * {@link StaticClassLoader} does not run.
     * 
     * @throws SetUpException unwanted.
     */
    @BeforeClass
    public static void loadLogicUtils() throws SetUpException {
        LogicUtils.initialize(new TestConfiguration(new Properties()));
    }
    
    /**
     * Tests whether results are simplified correctly. 
     * 
     * @throws SetUpException unwanted.
     */
    @Test
    @SuppressWarnings("null")
    public void testSimplify() throws SetUpException {
        TestConfiguration config = new TestConfiguration(new Properties());
        config.registerSetting(Settings.SIMPLIFIY);
        config.setValue(Settings.SIMPLIFIY, SimplificationType.FEATURE_EFFECTS);
        
        TestAnalysisComponentProvider<VariableWithFeatureEffect> input
            = new TestAnalysisComponentProvider<VariableWithFeatureEffect>(
                new VariableWithFeatureEffect("A=0", new Variable("C")),
                new VariableWithFeatureEffect("A=1", new Variable("C")),
                new VariableWithFeatureEffect("B=0", new Variable("D")),
                new VariableWithFeatureEffect("B=1", new Negation(new Variable("D")))
                );
        
        FeAggregator ag = new FeAggregator(config, input);
        
        VariableWithFeatureEffect fe = ag.getNextResult();
        assertThat(fe.getVariable(), is("A"));
        assertThat(fe.getFeatureEffect(), is(new Variable("C")));
        
        fe = ag.getNextResult();
        assertThat(fe.getVariable(), is("B"));
        assertThat(fe.getFeatureEffect(), is(True.INSTANCE));
        
        assertThat(ag.getNextResult(), nullValue());
    }

}
