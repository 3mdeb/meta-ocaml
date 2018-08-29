DESCRIPTION = "A Protobuf Compiler for OCaml"
SECTION = "devel"
LICENSE = "MIT"

DEPENDS += " \
    ocaml-native \
    opam-ocamlbuild \
    opam-ppx-deriving \
    opam-cppo \
    opam-cppo-ocamlbuild \
    opam-ppx-deriving-protobuf \
    "

DEPENDS_remove = " \
    ocaml-cross-${TARGET_ARCH} \
    "

inherit opam_install

#OPAM_VERBOSE = "1"
