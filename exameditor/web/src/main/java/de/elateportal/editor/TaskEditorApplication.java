package de.elateportal.editor;

import net.databinder.hib.DataApplication;

import org.hibernate.cfg.AnnotationConfiguration;


import de.elateportal.editor.pages.OverviewPage;
import de.thorstenberger.taskmodel.complex.complextaskdef.McSubTaskDef;

public class TaskEditorApplication extends DataApplication {

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.databinder.hib.DataApplication#configureHibernate(org.hibernate.cfg
     * .AnnotationConfiguration)
     */
    @Override
    protected void configureHibernate(final AnnotationConfiguration config) {
        super.configureHibernate(config);
        // add all model classes
        config.addAnnotatedClass(McSubTaskDef.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.AddonSubTaskDef.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef.Cloze.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef.Cloze.Gap.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef.Cloze.Gap.CorrectItem.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef.Cloze.TextOrGapItem.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef.GraphicalCloze.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef.GraphicalCloze.Gap.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef.GraphicalCloze.Gap.CorrectItem.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.AddonTaskBlock.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.AddonTaskBlock.AddonSubTaskDefOrChoiceItem.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.AddonTaskBlock.Choice.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.ClozeTaskBlock.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.ClozeTaskBlock.Choice.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.ClozeTaskBlock.ClozeConfig.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.ClozeTaskBlock.ClozeSubTaskDefOrChoiceItem.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.MappingTaskBlock.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.MappingTaskBlock.Choice.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.MappingTaskBlock.MappingConfig.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.MappingTaskBlock.MappingSubTaskDefOrChoiceItem.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.McTaskBlock.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.McTaskBlock.Choice.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.McTaskBlock.McConfig.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.McTaskBlock.McConfig.Different.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.McTaskBlock.McConfig.Regular.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.McTaskBlock.McSubTaskDefOrChoiceItem.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.McTaskBlockOrClozeTaskBlockOrTextTaskBlockItem.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.PaintTaskBlock.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.PaintTaskBlock.Choice.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.PaintTaskBlock.PaintSubTaskDefOrChoiceItem.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.TextTaskBlock.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.TextTaskBlock.Choice.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Category.TextTaskBlock.TextSubTaskDefOrChoiceItem.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Config.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Config.CorrectionMode.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Config.CorrectionMode.CorrectOnlyProcessedTasks.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Config.CorrectionMode.MultipleCorrectors.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Config.CorrectionMode.Regular.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Revisions.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef.Revisions.Revision.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.Config.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.MappingSubTaskDef.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.MappingSubTaskDef.Assignment.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.MappingSubTaskDef.Concept.class);
        config
                .addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.MappingSubTaskDef.Concept.CorrectAssignmentIDItem.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.McSubTaskDef.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.McSubTaskDef.Correct.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.McSubTaskDef.Incorrect.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.PaintSubTaskDef.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.PaintSubTaskDef.Images.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.PaintSubTaskDef.TextualAnswer.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefType.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.TaskBlockType.class);
        config.addAnnotatedClass(de.thorstenberger.taskmodel.complex.complextaskdef.TextSubTaskDef.class);
    }

    @Override
    public Class getHomePage() {
        return OverviewPage.class;
    }

}
