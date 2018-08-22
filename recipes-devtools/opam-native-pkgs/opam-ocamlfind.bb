DESCRIPTION = "Library to find OCaml packages - installed via opam"
SECTION = "devel"
LICENSE = "LGPLv2.1"

inherit opam_install

# ocamlfind is a host system package, therefore we need to include ocaml-native
DEPENDS += " ocaml-native"
DEPENDS_remove = "ocaml-cross-${TARGET_ARCH} opam-ocamlfind"

# more debug output, anyone?
#OPAM_VERBOSE = "1"
