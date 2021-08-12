package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

public class YamlManager {
	public static List<Object> readYaml(String yamlPath) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		InputStream inputStream = new FileInputStream(new File(yamlPath));
		LinkedHashMap<String, Object> yamlMap = yaml.load(inputStream);
		List<Object> listMap = new ArrayList<>();
		for (String key : yamlMap.keySet()) {
			listMap.add(yamlMap.get(key));
		}
		return listMap;
	}

	public static List<Object> readYamlFromResources(String yamlName) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		InputStream inputStream = YamlManager.class.getClassLoader().getResourceAsStream(yamlName);
		LinkedHashMap<String, Object> yamlMap = yaml.load(inputStream);
		List<Object> listMap = new ArrayList<>();
		for (String key : yamlMap.keySet()) {
			listMap.add(yamlMap.get(key));
		}
		return listMap;
	}
}
