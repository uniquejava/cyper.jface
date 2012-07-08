package hello.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kitten.core.C;
import org.kitten.core.io.FileHelper;
import org.kitten.core.io.ILineProcessor;
import org.kitten.core.util.StringUtil;

public class FmsRule implements Rule {
	private static String[] names;

	@Override
	public String[] getDesignatedTableTypes() {
		return new String[] { "VIEW", "ALIAS" };
	}

	@Override
	public String[] getDesignatedNames() {
		if (names == null) {
			final List<String> nameList = new ArrayList<String>();
			ILineProcessor processor = new ILineProcessor() {
				@Override
				public boolean processLine(String str) {
					if (StringUtil.isNotBlank(str)) {
						nameList.add(str.trim().toUpperCase());
					}
					return FileHelper.CONTINUE;
				}
			};
			File f = new File(C.UD + "/config/fms_table_names.txt");
			try {
				FileHelper.processFileByLine(f, "UTF-8", processor);
			} catch (Exception e) {
				e.printStackTrace();
			}
			names = new String[nameList.size()];
			nameList.toArray(names);
		}
		return names;
	}

	@Override
	public String getSchemaPattern() {
		return "DBEFMSVR";
	}
	
	@Override
	public String getTableNamePattern() {
		return "FMS%";
	}
}
