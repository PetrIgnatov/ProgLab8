#include <jni.h>
#include <QString>

static jstring QStr_to_jstr(JNIEnv* env, const QString& qstr) {
        jclass string_class = env->FindClass("java/lang/String");
        jmethodID string_constr = env->GetMethodID(string_class, "<init>", "(Ljava/lang/String;)V");
        jstring js = env->NewStringUTF(qstr.toStdString().c_str());
        return js;
}

