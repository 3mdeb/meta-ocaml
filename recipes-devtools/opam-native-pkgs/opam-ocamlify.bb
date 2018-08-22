DESCRIPTION = "Tool to include files in OCaml code - installed via opam"
SECTION = "devel"
LICENSE = "LGPLv2.1"

inherit opam_install

# ocamlify is a host system package, therefore we need to include ocaml-native
DEPENDS += " ocaml-native opam-ocamlbuild"
DEPENDS_remove = "ocaml-cross-${TARGET_ARCH}"

# more debug output, anyone?
#OPAM_VERBOSE = "1"
