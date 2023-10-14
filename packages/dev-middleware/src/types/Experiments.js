/**
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @flow strict-local
 * @format
 */

export type Experiments = $ReadOnly<{
  enableCustomDebuggerFrontend: boolean,
}>;

export type ExperimentsConfig = Partial<Experiments>;
