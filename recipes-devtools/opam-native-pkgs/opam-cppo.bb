DESCRIPTION = "Equivalent of the C preprocessor for OCaml programs"
SECTION = "devel"
LICENSE = "BSD-3-Clause"

inherit opam_install

# executables from this package run on the host system package, therefore we need
# to include ocaml-native instead of ocaml-cross
DEPENDS += " ocaml-native opam-dune"
DEPENDS_remove = "ocaml-cross-${TARGET_ARCH}"

# more debug output, anyone?
#OPAM_VERBOSE = "1"
