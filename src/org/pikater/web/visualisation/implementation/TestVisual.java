package org.pikater.web.visualisation.implementation;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.postgre.largeobject.PGLargeObjectAction;
import org.pikater.shared.database.util.ResultFormatter;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.visualisation.definition.result.DSVisOneResult;
import org.pikater.web.visualisation.implementation.generator.ComparisonPNGGenerator;
import org.pikater.web.visualisation.implementation.generator.base.Generator;

public class TestVisual {

	public static void main(String[] args) throws IOException {

		DSVisOneResult dummyResult = new DSVisOneResult(
				new IProgressDialogResultHandler() {
					@Override
					public void updateProgress(float percentage) {
					}

					@Override
					public void finished(IProgressDialogTaskResult result) {
					}

					@Override
					public void failed() {
					}
				}, Generator.DEFAULTCHARTSIZE, Generator.DEFAULTCHARTSIZE);

		long time = 0;
		JPADataSetLO iris1 = new ResultFormatter<JPADataSetLO>(
				DAOs.dataSetDAO.getByDescription("iris"))
				.getSingleResultWithNull();
		JPADataSetLO iris2 = new ResultFormatter<JPADataSetLO>(
				DAOs.dataSetDAO.getByDescription("modified-iris"))
				.getSingleResultWithNull();

		String attr1 = "sepallength";
		String attr2 = "petallength";
		String attr3 = "class";

		System.out.println("Generating SVG Comparison Chart for: "
				+ iris1.getDescription() + " and " + iris2.getDescription());
		time = System.currentTimeMillis();
		try {
			File iris1file = new PGLargeObjectAction(null)
					.downloadLOFromDB(iris1.getOID());
			File iris2file = new PGLargeObjectAction(null)
					.downloadLOFromDB(iris2.getOID());
			System.out.println("Iris 1 temp file: "
					+ iris1file.getAbsolutePath());
			System.out.println("Iris 2 temp file: "
					+ iris2file.getAbsolutePath());
			ComparisonPNGGenerator csvggiris = new ComparisonPNGGenerator(
					dummyResult,
					new PrintStream(
							"core/datasets/visual/sIRIS_sepallength_petallength_class_c.png"),
					iris1, iris2, iris1file, iris2file, attr1, attr1, attr2,
					attr2, attr3, attr3);
			csvggiris.create();
		} catch (Exception e) {
			System.out.println("Unexpected error occured: " + e.getMessage());
		}
		System.out.println("Finished in: "
				+ (System.currentTimeMillis() - time) + " ms");

	}
}
