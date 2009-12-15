package de.elateportal.editor;

import net.databinder.hib.DataApplication;

import org.hibernate.cfg.AnnotationConfiguration;

import de.elateportal.editor.pages.OverviewPage;

public class TaskEditorApplication extends DataApplication {
	/**
	 * @return
	 */
	public static TaskEditorApplication getInstance() {
		return (TaskEditorApplication) get();
	}

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
		config.addAnnotatedClass(de.elateportal.model.McSubTaskDef.class);
		config.addAnnotatedClass(de.elateportal.model.AddonSubTaskDef.class);
		config.addAnnotatedClass(de.elateportal.model.ClozeSubTaskDef.class);
		config.addAnnotatedClass(de.elateportal.model.ClozeSubTaskDef.Cloze.class);
		config.addAnnotatedClass(de.elateportal.model.ClozeSubTaskDef.Cloze.Gap.class);
		config.addAnnotatedClass(de.elateportal.model.ClozeSubTaskDef.Cloze.Gap.CorrectItem.class);
		config.addAnnotatedClass(de.elateportal.model.ClozeSubTaskDef.Cloze.TextOrGapItem.class);
		config.addAnnotatedClass(de.elateportal.model.ClozeSubTaskDef.GraphicalCloze.class);
		config.addAnnotatedClass(de.elateportal.model.ClozeSubTaskDef.GraphicalCloze.Gap.class);
		config.addAnnotatedClass(de.elateportal.model.ClozeSubTaskDef.GraphicalCloze.Gap.CorrectItem.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.AddonTaskBlock.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.AddonTaskBlock.AddonSubTaskDefOrChoiceItem.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.AddonTaskBlock.Choice.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.ClozeTaskBlock.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.ClozeTaskBlock.Choice.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.ClozeTaskBlock.ClozeConfig.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.ClozeTaskBlock.ClozeSubTaskDefOrChoiceItem.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.MappingTaskBlock.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.MappingTaskBlock.Choice.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.MappingTaskBlock.MappingConfig.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.MappingTaskBlock.MappingSubTaskDefOrChoiceItem.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.McTaskBlock.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.McTaskBlock.Choice.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.McTaskBlock.McConfig.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.McTaskBlock.McConfig.Different.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.McTaskBlock.McConfig.Regular.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.McTaskBlock.McSubTaskDefOrChoiceItem.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.McTaskBlockOrClozeTaskBlockOrTextTaskBlockItem.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.PaintTaskBlock.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.PaintTaskBlock.Choice.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.PaintTaskBlock.PaintSubTaskDefOrChoiceItem.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.TextTaskBlock.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.TextTaskBlock.Choice.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Category.TextTaskBlock.TextSubTaskDefOrChoiceItem.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Config.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Config.CorrectionMode.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Config.CorrectionMode.CorrectOnlyProcessedTasks.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Config.CorrectionMode.MultipleCorrectors.class);
		config
		    .addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Config.CorrectionMode.Regular.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Revisions.class);
		config.addAnnotatedClass(de.elateportal.model.ComplexTaskDef.Revisions.Revision.class);
		config.addAnnotatedClass(de.elateportal.model.Config.class);
		config.addAnnotatedClass(de.elateportal.model.MappingSubTaskDef.class);
		config.addAnnotatedClass(de.elateportal.model.MappingSubTaskDef.Assignment.class);
		config.addAnnotatedClass(de.elateportal.model.MappingSubTaskDef.Concept.class);
		config
		    .addAnnotatedClass(de.elateportal.model.MappingSubTaskDef.Concept.CorrectAssignmentIDItem.class);
		config.addAnnotatedClass(de.elateportal.model.McSubTaskDef.class);
		config.addAnnotatedClass(de.elateportal.model.McSubTaskDef.Correct.class);
		config.addAnnotatedClass(de.elateportal.model.McSubTaskDef.Incorrect.class);
		config.addAnnotatedClass(de.elateportal.model.PaintSubTaskDef.class);
		config.addAnnotatedClass(de.elateportal.model.PaintSubTaskDef.Images.class);
		config.addAnnotatedClass(de.elateportal.model.PaintSubTaskDef.TextualAnswer.class);
		config.addAnnotatedClass(de.elateportal.model.SubTaskDefType.class);
		config.addAnnotatedClass(de.elateportal.model.TaskBlockType.class);
		config.addAnnotatedClass(de.elateportal.model.TextSubTaskDef.class);
	}

	@Override
	public Class getHomePage() {
		return OverviewPage.class;
	}

	@Override
	protected void init() {
		super.init();
		// System.out.println(AbstractLink.class.getProtectionDomain().getCodeSource());
		// for (final Method m : AbstractLink.class.getDeclaredMethods()) {
		// System.out.println(m);
		// }
		getMarkupSettings().setStripWicketTags(true);
	}

}
