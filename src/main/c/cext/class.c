#include <truffleruby-impl.h>

// Class, rb_class_*, rb_mod_*, rb_cvar_*, rb_cv_*

VALUE rb_class_inherited_p(VALUE module, VALUE object) {
  return RUBY_CEXT_INVOKE("rb_class_inherited_p", module, object);
}

const char* rb_class2name(VALUE ruby_class) {
  return RSTRING_PTR(rb_class_name(ruby_class));
}

VALUE rb_class_real(VALUE ruby_class) {
  if (!ruby_class) {
    return NULL;
  }
  return RUBY_CEXT_INVOKE("rb_class_real", ruby_class);
}

VALUE rb_class_superclass(VALUE ruby_class) {
  return RUBY_INVOKE(ruby_class, "superclass");
}

VALUE rb_obj_class(VALUE object) {
  return rb_class_real(rb_class_of(object));
}

VALUE rb_singleton_class(VALUE object) {
  return RUBY_INVOKE(object, "singleton_class");
}

VALUE rb_class_of(VALUE object) {
  return RUBY_CEXT_INVOKE("rb_class_of", object);
}

VALUE rb_obj_alloc(VALUE ruby_class) {
  return RUBY_INVOKE(ruby_class, "__allocate__");
}

VALUE rb_class_path(VALUE ruby_class) {
  return RUBY_INVOKE(ruby_class, "name");
}

VALUE rb_path2class(const char *string) {
  return RUBY_CEXT_INVOKE("rb_path_to_class", rb_str_new_cstr(string));
}

VALUE rb_path_to_class(VALUE pathname) {
  return RUBY_CEXT_INVOKE("rb_path_to_class", pathname);
}

VALUE rb_class_name(VALUE ruby_class) {
  VALUE name = RUBY_INVOKE(ruby_class, "name");

  if (NIL_P(name)) {
    return rb_class_name(rb_obj_class(ruby_class));
  } else {
    return name;
  }
}

VALUE rb_class_new(VALUE super) {
  return RUBY_CEXT_INVOKE("rb_class_new", super);
}

VALUE rb_class_new_instance(int argc, const VALUE *argv, VALUE klass) {
  return RUBY_CEXT_INVOKE("rb_class_new_instance", klass, rb_ary_new4(argc, argv));
}

VALUE rb_cvar_defined(VALUE klass, ID id) {
  return RUBY_CEXT_INVOKE("rb_cvar_defined", klass, ID2SYM(id));
}

VALUE rb_cvar_get(VALUE klass, ID id) {
  return RUBY_CEXT_INVOKE("rb_cvar_get", klass, ID2SYM(id));
}

void rb_cvar_set(VALUE klass, ID id, VALUE val) {
  RUBY_CEXT_INVOKE_NO_WRAP("rb_cvar_set", klass, ID2SYM(id), val);
}

VALUE rb_cv_get(VALUE klass, const char *name) {
  return RUBY_CEXT_INVOKE("rb_cv_get", klass, rb_str_new_cstr(name));
}

void rb_cv_set(VALUE klass, const char *name, VALUE val) {
  RUBY_CEXT_INVOKE_NO_WRAP("rb_cv_set", klass, rb_str_new_cstr(name), val);
}

void rb_define_attr(VALUE klass, const char *name, int read, int write) {
  polyglot_invoke(RUBY_CEXT, "rb_define_attr", rb_tr_unwrap(klass), rb_tr_unwrap(ID2SYM(rb_intern(name))), read, write);
}

void rb_define_class_variable(VALUE klass, const char *name, VALUE val) {
  RUBY_CEXT_INVOKE_NO_WRAP("rb_cv_set", klass, rb_str_new_cstr(name), val);
}

VALUE rb_mod_ancestors(VALUE mod) {
  return RUBY_INVOKE(mod, "ancestors");
}

VALUE rb_module_new(void) {
  return RUBY_CEXT_INVOKE("rb_module_new");
}

VALUE rb_newobj_of(VALUE klass, VALUE flags) {
  // ignore flags for now
  return RUBY_CEXT_INVOKE("rb_newobj_of", klass);
}

void rb_extend_object(VALUE object, VALUE module) {
  RUBY_INVOKE(module, "extend_object", object);
}

VALUE rb_class_instance_methods(int argc, const VALUE *argv, VALUE mod) {
  if (rb_check_arity(argc, 0, 1)) {
    VALUE include_super = rb_boolean(argv[0]);
    return RUBY_INVOKE(mod, "instance_methods", include_super);
  } else {
    return RUBY_INVOKE(mod, "instance_methods");
  }
}

VALUE rb_class_public_instance_methods(int argc, const VALUE *argv, VALUE mod) {
  if (rb_check_arity(argc, 0, 1)) {
    VALUE include_super = rb_boolean(argv[0]);
    return RUBY_INVOKE(mod, "public_instance_methods", include_super);
  } else {
    return RUBY_INVOKE(mod, "public_instance_methods");
  }
}

VALUE rb_class_protected_instance_methods(int argc, const VALUE *argv, VALUE mod) {
  if (rb_check_arity(argc, 0, 1)) {
    VALUE include_super = rb_boolean(argv[0]);
    return RUBY_INVOKE(mod, "protected_instance_methods", include_super);
  } else {
    return RUBY_INVOKE(mod, "protected_instance_methods");
  }
}

VALUE rb_class_private_instance_methods(int argc, const VALUE *argv, VALUE mod) {
  if (rb_check_arity(argc, 0, 1)) {
    VALUE include_super = rb_boolean(argv[0]);
    return RUBY_INVOKE(mod, "private_instance_methods", include_super);
  } else {
    return RUBY_INVOKE(mod, "private_instance_methods");
  }
}