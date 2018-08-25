DESCRIPTION = "Compatibility Result module"
SECTION = "devel"
LICENSE = "BSD3"

inherit opam_install

# executables from this package run on the host system package, therefore we need
# to include ocaml-native instead of ocaml-cross
DEPENDS += " ocaml-native opam-dune"
DEPENDS_remove = "ocaml-cross-${TARGET_ARCH}"


OPAM_VERBOSE = "1"
