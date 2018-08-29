DESCRIPTION = "Unit testing framework loosely based on HUnit"
SECTION = "devel"
LICENSE = "Proprietary"

DEPENDS += " \
    ocaml-native \
    opam-ocamlbuild \
    "

DEPENDS_remove = " \
    ocaml-cross-${TARGET_ARCH} \
    "

inherit opam_install

#OPAM_VERBOSE = "1"
