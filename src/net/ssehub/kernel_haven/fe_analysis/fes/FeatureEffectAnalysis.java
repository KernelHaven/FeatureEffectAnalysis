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

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.analysis.AnalysisComponent;
import net.ssehub.kernel_haven.analysis.PipelineAnalysis;
import net.ssehub.kernel_haven.config.Configuration;
import net.ssehub.kernel_haven.fe_analysis.pcs.PcFinder;
import net.ssehub.kernel_haven.util.null_checks.NonNull;

/**
 * An analysis that finds feature effect formulas for variables.
 * 
 * @author Adam
 */
public class FeatureEffectAnalysis extends PipelineAnalysis {

    /**
     * Creates a new {@link FeatureEffectAnalysis}.
     * 
     * @param config The global configuration.
     */
    public FeatureEffectAnalysis(@NonNull Configuration config) {
        super(config);
    }

    @Override
    protected @NonNull AnalysisComponent<?> createPipeline() throws SetUpException {
        return new NonBooleanFeExpander(config,
                new FeatureEffectFinder(config,
                    new PcFinder(config,
                            getCmComponent(),
                            getBmComponent()
                    )
                )
        );
    }

}
