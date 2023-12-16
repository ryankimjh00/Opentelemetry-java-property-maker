package com.example.MercenarySys.OpentelemetryRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 역할: 패키지 내의 모든 Class 의 Method 를 추출 하고 파일에 저장 하는 클래스
 * 결과: 추출된 Method 는 [패키지 경로].[클래스 명] [메소드 명 1,메소드 명 2,...]; 형태로 출력 된다.
 * 추출된 Method 는 extractedMethod.txt 파일에 저장 된다.
 * 주의: 이 class 는 반드시 main method 가 있는 패키지 경로에 저장 되어야 한다.
 * Method 내부 에서 nested Method 등을 필터링 하며, '$'가 있는지 잘 확인 해야 한다.
 * Now the Time Complexity is O(n^3) -> O(n^2) -> O(mn) (m: method count, n: class count)
 */
public class OtelMethodExtractor {
    private static final Logger logger = LoggerFactory.getLogger(OtelMethodExtractor.class);
    private final String packageName;
    private StringBuilder escapeDeletedValue;

    public OtelMethodExtractor(String packageName) {
        this.packageName = packageName;
    }

    public String extractAndWriteMethods() { // 메소드 추출해서 otel.properties에 넣을 수 있게 가공
        List<Class<?>> classes = getClassesForPackage(packageName);
        for (Class<?> clazz : classes) {
            List<String> methodNames = extractValidMethodNames(clazz);
            if (!methodNames.isEmpty()) {
                String methodListString = "[" + String.join(",", methodNames) + "];\\";
                //String resultForPrint = "  " + clazz.getName() + methodListString; # println으로 출력될 때
                String blankDeletedValue = clazz.getName() + methodListString;
                escapeDeletedValue = new StringBuilder(escapeDeletedValue + blankDeletedValue.substring(0, blankDeletedValue.length() - 1));
            }
        }
        return String.valueOf(escapeDeletedValue).replace("null", "");
    }

    private List<String> extractValidMethodNames(Class<?> clazz) {
        List<String> methodNames = new ArrayList<>();
        if (clazz == null) {
            logger.error("Class is null. Cannot extract method names.");
            return methodNames; // 빈 목록 반환 또는 예외 던지기
        }
        for (Method method : clazz.getDeclaredMethods()) {
            try {
                String methodName = method.getName();
                if (isValidMethodName(methodName)) {
                    methodNames.add(methodName);
                }
            } catch (SecurityException e) {
                logger.error("Error extracting method name: " + e.getMessage());
            }
        }
        return methodNames;
    }

    private boolean isValidMethodName(String methodName) {
        boolean isValid = !methodName.contains("$") && !methodName.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
        if (!isValid) {
            logger.warn("Found invalid method name: [" + methodName + "]...Skipping...");
        }
        return isValid;
    }

    private List<Class<?>> getClassesForPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        File packageDirectory = getPackageDirectory(packageName);

        if (!packageDirectory.exists()) {
            return classes;
        }

        for (File file : Objects.requireNonNull(packageDirectory.listFiles())) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                if (isNotInnerClass(className)) {
                    classes.add(loadClass(className));
                }
            } else if (file.isDirectory()) {
                classes.addAll(getClassesForPackage(packageName + "." + file.getName()));
            }
        }
        return classes;
    }

    private File getPackageDirectory(String packageName) { // 패키지 경로를 가져옴
        return new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"))).getFile());
    }

    private boolean isNotInnerClass(String className) { // 내부 클래스가 아닌지 확인
        return !className.contains("$") && !className.contains("typehandler");
    }

    private Class<?> loadClass(String className) { // 클래스 로드
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("에러가 발생했습니다. Error: " + e.getMessage(), e);
            return null;
        }
    }
}
